package com.mert.arabaykamarezv

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

class MusteriBilgiActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_musteri_bilgi)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = getMenuInflater()
        getMenuInflater().inflate(R.menu.musteri_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.add_musteri_item){
            val intent = Intent(this,MusteriJobAddActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

}