package com.mert.arabaykamarezv.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.mert.arabaykamarezv.R
import com.mert.arabaykamarezv.databinding.ActivityArabaKonumBinding
import com.mert.arabaykamarezv.model.Place
import com.mert.arabaykamarezv.roomdb.PlaceDao
import com.mert.arabaykamarezv.roomdb.PlaceDatabase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.lang.Exception

class ArabaKonumActivity : AppCompatActivity(), OnMapReadyCallback ,GoogleMap.OnMapLongClickListener{

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityArabaKonumBinding
    private lateinit var locationManager : LocationManager
    private lateinit var locationListener: LocationListener
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private lateinit var activityResultLauncher : ActivityResultLauncher<Intent>
    private lateinit var sharedPreferences: SharedPreferences
    var trackBoolean : Boolean? = null
    private var selectedLatitude : Double? = null
    private var selectedLongitude : Double? = null
    private lateinit var db : PlaceDatabase
    private lateinit var placeDao : PlaceDao
    val compositeDisposable = CompositeDisposable()
    var placeFromMain : Place? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityArabaKonumBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        registerLauncher()
        sharedPreferences = this.getSharedPreferences("com.mert.arabaykamarezv", MODE_PRIVATE)
        trackBoolean = false
        selectedLatitude = 0.0
        selectedLongitude = 0.0

        db = Room.databaseBuilder(applicationContext,PlaceDatabase::class.java,"Places").fallbackToDestructiveMigration().build()
        placeDao = db.placeDao()
        binding.btnSaveKonum.isEnabled = false
        this.setTitle("Araç Ekle")
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMapLongClickListener(this)
        val intent = intent
        val info = intent.getStringExtra("info")
        if(info == "new"){
            binding.btnSaveKonum.visibility= View.VISIBLE
            binding.btnDelete.visibility = View.GONE
            try {
                locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            } catch (e: Exception) {
                e.printStackTrace()
            }
            locationListener = object : LocationListener{
                override fun onLocationChanged(location: Location) {
                    trackBoolean = sharedPreferences.getBoolean("trackBoolean", false)
                    if(trackBoolean == false){
                        val userLocation = LatLng(location.latitude,location.longitude)
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,15f))
                        sharedPreferences.edit().putBoolean("trackBoolean",true).apply()
                    }

                }

            }
            if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.ACCESS_FINE_LOCATION)){
                    Snackbar.make(binding.root,"Permission needed for location", Snackbar.LENGTH_INDEFINITE).setAction("Give permission"){
                        //request permission
                        permissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
                    }.show()

                }else{
                    // request permission
                    permissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
                }
            }else{
                //permission granted
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0f,locationListener)
                val lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (lastLocation != null){
                    val lastUserLocation = LatLng(lastLocation.latitude,lastLocation.longitude)
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation,15f))
                }
                mMap.isMyLocationEnabled=true
            }
        }else{
            mMap.clear()
            placeFromMain= intent.getSerializableExtra("selectedPlace") as Place
            placeFromMain?.let {
                val latLng = LatLng(it.latitude,it.longitude)
                mMap.addMarker(MarkerOptions().position(latLng).title(it.plaka))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15f))
                binding.txtPlakaKonum.setText(it.plaka)
                binding.btnSaveKonum.visibility = View.GONE
                binding.btnDelete.visibility = View.VISIBLE            }

        }

        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0f,locationListener)

    }
    private fun registerLauncher() {
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            if (result) {
                //permission granted
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)
                    val lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    if (lastLocation != null) {
                        val lastUserLocation = LatLng(lastLocation.latitude, lastLocation.longitude)
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastUserLocation, 15f))
                    }
                }
            } else {
                //permission denied
                Toast.makeText(this, "Permisson needed!", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onMapLongClick(p0: LatLng) {
    mMap.clear()
        mMap.addMarker(MarkerOptions().position(p0))
        selectedLatitude = p0.latitude
        selectedLongitude = p0.longitude
        binding.btnSaveKonum.isEnabled = true
    }
    fun KonumKaydet(view : View){
        val place = Place(binding.txtPlakaKonum.text.toString(),selectedLatitude!!,selectedLongitude!!)
        compositeDisposable.add(
            placeDao.insert(place)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse)
        )

}
    private fun handleResponse(){
        val intent = Intent(this,MusteriKonumRecycleActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }
    fun KonumSil(view : View){
        placeFromMain?.let {
            compositeDisposable.add(
                placeDao.delete(it)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::handleResponse)
            )
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}