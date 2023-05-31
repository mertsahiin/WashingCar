package com.mert.arabaykamarezv.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mert.arabaykamarezv.databinding.ActivityMusteriGirisBinding
import com.mert.arabaykamarezv.databinding.MusterirecyclerrowBinding

class MusteriGirisActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMusteriGirisBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusteriGirisBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth = Firebase.auth
        this.setTitle("Müşteri Giriş")
    }

    fun Musterisignin(view: View) {
        println("Onayy")
        val eMail = binding.txtMusteriMail.text.toString()
        val password = binding.txtMusteriPassword.text.toString()
        if (eMail.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Bos Alan Bırakma", Toast.LENGTH_LONG).show()
            println("Yanlıs")
        } else {
            println("Doğru")
            auth.signInWithEmailAndPassword(eMail, password).addOnSuccessListener {

                val intent = Intent(this, MusterirecyclerrowBinding::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener {
                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun Musterisignup(view: View) {
        println("Onayy")
        val eMail = binding.txtMusteriMail.text.toString()
        val password = binding.txtMusteriPassword.text.toString()
        if (eMail.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Bos Alan Bırakma", Toast.LENGTH_LONG).show()
            println("Yanlıs")
        } else {
            println("Doğru")
            auth.createUserWithEmailAndPassword(eMail, password).addOnSuccessListener {
                println("oldu")
                val intent = Intent(this, MusteriGirisActivity::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener {
                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
    }
}