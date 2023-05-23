package com.mert.arabaykamarezv.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.mert.arabaykamarezv.R
import com.mert.arabaykamarezv.adapter.PlaceAdapter
import com.mert.arabaykamarezv.databinding.ActivityMainBinding
import com.mert.arabaykamarezv.databinding.ActivityMusteriKonumRecycleBinding
import com.mert.arabaykamarezv.model.Place
import com.mert.arabaykamarezv.roomdb.PlaceDatabase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MusteriKonumRecycleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMusteriKonumRecycleBinding
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusteriKonumRecycleBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val db = Room.databaseBuilder(this,PlaceDatabase::class.java,"Places").fallbackToDestructiveMigration().build()
        val placeDao = db.placeDao()
        compositeDisposable.add(
            placeDao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handlerResponse)
        )
        this.setTitle("Paylaşılan İşler")
    }
    private fun handlerResponse(placeList : List<Place>){
        binding.recyclerViewKonum.layoutManager = LinearLayoutManager(this)
        val adapter = PlaceAdapter(placeList)
        binding.recyclerViewKonum.adapter=adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuinflater = menuInflater
        menuinflater.inflate(R.menu.plaka_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.add_konum){
            val intent = Intent(this, ArabaKonumActivity::class.java)
            intent.putExtra("info","new")
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)

    }
}