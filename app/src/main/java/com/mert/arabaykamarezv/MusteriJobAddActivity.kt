package com.mert.arabaykamarezv

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.mert.arabaykamarezv.databinding.ActivityMusteriJobAdd2Binding
import com.mert.arabaykamarezv.model.musteriilan
import java.io.ByteArrayOutputStream
import java.lang.Exception

class MusteriJobAddActivity : AppCompatActivity(), OnMapReadyCallback,GoogleMap.OnMapLongClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMusteriJobAdd2Binding
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
    private lateinit var permissionLauncher:ActivityResultLauncher<String>
    private lateinit var sharedPreferences: SharedPreferences
    private var trackBoolean : Boolean?=null
    private var selectedLatitude : Double? = null
    private var selectedLongitude : Double? = null
    private lateinit var activityResultLauncher : ActivityResultLauncher<Intent>
    var mselectedBitmap : Bitmap? = null
    private lateinit var database : SQLiteDatabase



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMusteriJobAdd2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        database = this.openOrCreateDatabase("Jobs", Context.MODE_PRIVATE,null)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        registerLauncher()
        val intent = intent
        val info = intent.getStringExtra("info")

        if(info.equals("new")){

            binding.Plakatext.setText("")
            binding.MusteriSaveBtn.visibility = View.VISIBLE
            binding.MusteriDelBtn.visibility = View.VISIBLE
            val selectedImageBackGround = BitmapFactory.decodeResource(applicationContext.resources,R.drawable.indir)
            binding.imageView2.setImageBitmap(selectedImageBackGround)
        }else{
            //binding.MusteriSaveBtn.visibility = View.INVISIBLE
            binding.MusteriDelBtn.visibility = View.INVISIBLE
            val selectedId = intent.getIntExtra("id" , 1)
            val cursor = database.rawQuery("SELECT * FROM mjobs WHERE id = ?", arrayOf(selectedId.toString()))
            val PlakaIx = cursor.getColumnIndex("Plaka")

            val imageIx = cursor.getColumnIndex("image")

            while(cursor.moveToNext()){

                binding.Plakatext.setText(cursor.getString(PlakaIx))
                val byteArray = cursor.getBlob(imageIx)
                val bitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
                binding.imageView2.setImageBitmap(bitmap)
            }
            cursor.close()

        }

        sharedPreferences=this.getSharedPreferences("com.mert.arabaykamarezv", MODE_PRIVATE)
        trackBoolean=false
        selectedLatitude= 0.0
        selectedLongitude=0.0


    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMapLongClickListener(this)
        //casting
        locationManager = this.getSystemService(LOCATION_SERVICE) as LocationManager

        locationListener = object :LocationListener{
            override fun onLocationChanged(location: Location) {
                trackBoolean = sharedPreferences.getBoolean("trackBoolean",false)
                if(trackBoolean==false) {
                    val userlocation = LatLng(location.latitude, location.longitude)
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userlocation, 15f))
                    sharedPreferences.edit().putBoolean("trackBoolean",true).apply()

                }
                }
        }
        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.ACCESS_FINE_LOCATION)){
                Snackbar.make(binding.root,"Permission needed for location",Snackbar.LENGTH_INDEFINITE).setAction("Give permission"){
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

       // locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0f,locationListener)

    }

    private fun registerLauncher(){
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == RESULT_OK){
                val intentFromResult = result.data
                if(intentFromResult != null){
                    val imageData = intentFromResult.data
                    if(imageData != null){
                        try {
                            if(Build.VERSION.SDK_INT <= 28){
                                val source = ImageDecoder.createSource(this.contentResolver,imageData)
                                mselectedBitmap = ImageDecoder.decodeBitmap(source)
                                binding.imageView2.setImageBitmap(mselectedBitmap)
                            }else{
                                mselectedBitmap = MediaStore.Images.Media.getBitmap(contentResolver,imageData)
                                binding.imageView2.setImageBitmap(mselectedBitmap)
                            }
                        }catch (e : Exception){
                            e.printStackTrace()
                        }

                    }

                }
            }
        }
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){ result ->

            if(result){
                val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            }else{
                Toast.makeText(this,"Permission Needed",Toast.LENGTH_LONG).show()
            }
        }
    }

    fun MusteriImage(view : View){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                Snackbar.make(view,"Permission Needed For Gallery",Snackbar.LENGTH_INDEFINITE).setAction("Give Permission",View.OnClickListener {
                    //izin iste
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }).show()
            }
            else{
                // izin iste
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
        else{
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activityResultLauncher.launch(intent)

        }
    }


    override fun onMapLongClick(p0: LatLng) {
        mMap.clear()
        mMap.addMarker(MarkerOptions().position(p0))
        selectedLatitude=p0.latitude
        selectedLongitude=p0.longitude

    }
    private fun MakeSmallerBitmap(image : Bitmap , maximumSize : Int) : Bitmap{
        var widht = image.width
        var height = image.height
        val bitmapRatio : Double = widht.toDouble() / height.toDouble()
        if(bitmapRatio < 1 ){
            //dikey
            height = maximumSize
            val scaledWidth = height * bitmapRatio
            widht = scaledWidth.toInt()
        }
        else{
            //yatay
            widht = maximumSize
            val scaledHeight = widht / bitmapRatio
            height = scaledHeight.toInt()
        }
        return Bitmap.createScaledBitmap(image,widht,height,true)


    }

    fun musterisave(view: View){
        val Plaka = binding.Plakatext.text.toString()

        if(mselectedBitmap != null){
            val smallBitmap = MakeSmallerBitmap(mselectedBitmap!!,300)
            val outPutStream = ByteArrayOutputStream()
            smallBitmap.compress(Bitmap.CompressFormat.PNG,50,outPutStream)
            val byteArray = outPutStream.toByteArray()

            try{
                //val database = this.openOrCreateDatabase("Arts", MODE_PRIVATE,null)
                database.execSQL("CREATE TABLE IF NOT EXISTS mjobs (id INTEGER PRIMARY KEY, Plaka VARCHAR,image BLOB )")
                val sqlString = "INSERT INTO mjobs(Plaka,image) VALUES(?,?)"
                val statement = database.compileStatement(sqlString)
                statement.bindString(1,Plaka)

                statement.bindBlob(2,byteArray)
                statement.execute()

            }catch ( e : Exception){
                e.printStackTrace()
            }
            val intent = Intent(this,MusteriBilgiActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

    }

    fun MusteriDel(view : View){

    }



}