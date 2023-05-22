package com.mert.arabaykamarezv.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.mert.arabaykamarezv.R
import com.mert.arabaykamarezv.adapter.JobAdapter
import com.mert.arabaykamarezv.databinding.ActivityAdminInfoBinding
import com.mert.arabaykamarezv.model.Jobs

class AdminInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminInfoBinding
    private lateinit var JobList : ArrayList<Jobs>
    private lateinit var JobAdapter : JobAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminInfoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        JobList = ArrayList<Jobs>()
        JobAdapter = JobAdapter(JobList)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = JobAdapter


        try{
            val database  = this.openOrCreateDatabase("Jobs", MODE_PRIVATE,null)
            val cursor = database.rawQuery("SELECT * FROM jobs" ,null)
            val JobInfoIx = cursor.getColumnIndex("JobInfo")
            val IdIx = cursor.getColumnIndex("id")
            while(cursor.moveToNext()){
                val name = cursor.getString(JobInfoIx)
                val id = cursor.getInt(IdIx)
                val job = Jobs(name,id)
                JobList.add(job)
            }
            JobAdapter.notifyDataSetChanged()
            cursor.close()
        }catch (e : Exception){
            e.printStackTrace()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.job_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.add_job_item){
            val intent = Intent(this, AdminJobActivity::class.java)
            intent.putExtra("info" ,"new")
            startActivity(intent)
        }

        return super.onOptionsItemSelected(item)
    }
    fun iseGit(view : View){
        val intent = Intent(this,MusteriKonumRecycleActivity::class.java)
        startActivity(intent)
    }
}