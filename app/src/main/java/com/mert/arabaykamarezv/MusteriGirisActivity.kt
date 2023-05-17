package com.mert.arabaykamarezv

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mert.arabaykamarezv.databinding.ActivityMusteriGirisBinding

class MusteriGirisActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMusteriGirisBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusteriGirisBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}