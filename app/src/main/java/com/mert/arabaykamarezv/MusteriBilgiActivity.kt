package com.mert.arabaykamarezv

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.mert.arabaykamarezv.databinding.ActivityMainBinding
import com.mert.arabaykamarezv.databinding.ActivityMusteriBilgiBinding

class MusteriBilgiActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMusteriBilgiBinding
    private lateinit var MjobList : ArrayList<MüsteriJobs>
    private lateinit var mjobsAdapter : MjobsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusteriBilgiBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

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
        if(item.itemId==R.id.add_musteri_item){
            val intent = Intent(this,MusteriJobAddActivity::class.java)
            intent.putExtra("info","new")
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

}