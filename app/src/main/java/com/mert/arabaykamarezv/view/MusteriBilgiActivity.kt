package com.mert.arabaykamarezv.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.mert.arabaykamarezv.R
import com.mert.arabaykamarezv.adapter.MjobsAdapter
import com.mert.arabaykamarezv.databinding.ActivityMusteriBilgiBinding
import com.mert.arabaykamarezv.model.MüsteriJobs

class MusteriBilgiActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMusteriBilgiBinding
    private lateinit var MjobList : ArrayList<MüsteriJobs>
    private lateinit var mjobsAdapter : MjobsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusteriBilgiBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        this.setTitle("Müşteri Bilgi")

        MjobList = ArrayList<MüsteriJobs>()
        mjobsAdapter = MjobsAdapter(MjobList)
        binding.MusteriBilgiRC.layoutManager = LinearLayoutManager(this)
        binding.MusteriBilgiRC.adapter = mjobsAdapter

        try {
            val database = this.openOrCreateDatabase("Jobs", Context.MODE_PRIVATE,null)
            val cursor = database.rawQuery("Select * From mjobs",null)
            val PlakaIx = cursor.getColumnIndex("Plaka")
            val idIx = cursor.getColumnIndex("id")
            while (cursor.moveToNext()){
                val Plaka = cursor.getString(PlakaIx)
                val id = cursor.getInt(idIx)
                val Mjob = MüsteriJobs(Plaka,id)
                MjobList.add(Mjob)
            }
            mjobsAdapter.notifyDataSetChanged()
            cursor.close()

        }catch (e : Exception){
            e.printStackTrace()
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.musteri_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId== R.id.add_musteri_item){
            val intent = Intent(this, MusteriJobAddActivity::class.java)
            intent.putExtra("info","new")
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

    fun Konumagit(view : View){
        val intent = Intent(this, MusteriKonumRecycleActivity::class.java)
        startActivity(intent)
    }

    fun isegit(view : View){
        val intent = Intent(this, AdminInfoActivity::class.java)
        startActivity(intent)
    }

}