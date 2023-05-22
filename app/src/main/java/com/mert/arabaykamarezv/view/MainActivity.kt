package com.mert.arabaykamarezv.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.mert.arabaykamarezv.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    fun adminGiris(view : View){
    val intent = Intent(this, AdminGirisActivity::class.java)
        startActivity(intent)

    }

    fun musteriGiris(view : View){
        val intent = Intent(this, MusteriGirisActivity::class.java)
        startActivity(intent)

    }
}