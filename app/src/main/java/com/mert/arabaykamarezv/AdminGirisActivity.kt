package com.mert.arabaykamarezv

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mert.arabaykamarezv.databinding.ActivityAdminGirisBinding

class AdminGirisActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminGirisBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminGirisBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth = Firebase.auth
    }

        fun SignInAdmin(view : View){
            println("Onayy")
            val eMail = binding.txtadminMail.text.toString()
            val password = binding.txtadminPassword.text.toString()
            if(eMail.isEmpty() || password.isEmpty()){
                Toast.makeText(this,"Bos Alan Bırakma",Toast.LENGTH_LONG).show()
                println("Yanlıs")
            }else{
                println("Doğru")
                auth.signInWithEmailAndPassword(eMail,password).addOnSuccessListener {

                    val intent = Intent(this,AdminInfoActivity::class.java)
                    startActivity(intent)
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
                }
            }
        }
        fun SignUpAdmin(view : View){
            println("Onayy")
            val eMail = binding.txtadminMail.text.toString()
            val password = binding.txtadminPassword.text.toString()
            if(eMail.isEmpty() || password.isEmpty()){
                Toast.makeText(this,"Bos Alan Bırakma",Toast.LENGTH_LONG).show()
                println("Yanlıs")
            }else{
                println("Doğru")
                auth.createUserWithEmailAndPassword(eMail,password).addOnSuccessListener {
                    println("oldu")
                    val intent = Intent(this,AdminInfoActivity::class.java)
                    startActivity(intent)
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
                }
            }
        }
    }
