package com.mert.arabaykamarezv.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mert.arabaykamarezv.adapter.KriptoAdapter
import com.mert.arabaykamarezv.databinding.ActivityMainBinding
import com.mert.arabaykamarezv.model.CryptoModel
import com.mert.arabaykamarezv.roomdb.CryptoAPI
import io.reactivex.rxjava3.disposables.CompositeDisposable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(),KriptoAdapter.Listener {
    private lateinit var binding: ActivityMainBinding
    private val BASE_URL = "https://raw.githubusercontent.com/"
    private var cryptoModels: ArrayList<CryptoModel>? = null
    private var  kriptoAdapter: KriptoAdapter? = null

    //Disposable
    private var compositeDisposable: CompositeDisposable? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        this.setTitle("Giriş")
        compositeDisposable = CompositeDisposable()
        val layoutmanager : RecyclerView.LayoutManager = LinearLayoutManager(this)
        binding.recylerView.layoutManager = layoutmanager

        loadData()
    }
    private fun loadData(){
        val retrofit = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
        val service = retrofit.create(CryptoAPI::class.java)
        val call = service.getData()
        call.enqueue(object : Callback<List<CryptoModel>> {
            override fun onResponse(
                call: Call<List<CryptoModel>>,
                response: Response<List<CryptoModel>>
            ) {
                if(response.isSuccessful){
                    response.body()?.let {

                        cryptoModels = ArrayList(it)
                        kriptoAdapter = KriptoAdapter(cryptoModels!!, this@MainActivity)
                        binding.recylerView.adapter = kriptoAdapter
                    }
                }
            }

            override fun onFailure(call: Call<List<CryptoModel>>, t: Throwable) {
                t.printStackTrace()
            }

        })

    }
    private fun handleResponse(cryptoList : List<CryptoModel>){
        cryptoModels = ArrayList(cryptoList)

        cryptoModels?.let {
            kriptoAdapter = KriptoAdapter(it,this@MainActivity)
            binding.recylerView.adapter = kriptoAdapter
        }
    }
    override fun onDestroy() {
        super.onDestroy()

        compositeDisposable?.clear()
    }

    fun adminGiris(view : View){
        val intent = Intent(this, AdminGirisActivity::class.java)
        startActivity(intent)

    }

    fun musteriGiris(view : View){
        val intent = Intent(this, MusteriGirisActivity::class.java)
        startActivity(intent)

    }

    override fun onItemClick(cryptoModel: CryptoModel) {
        Toast.makeText(this,"Clicked ${cryptoModel.currency}", Toast.LENGTH_LONG).show()
    }
}