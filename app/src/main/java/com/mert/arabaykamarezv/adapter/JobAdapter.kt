package com.mert.arabaykamarezv.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mert.arabaykamarezv.databinding.RecyclerRowBinding
import com.mert.arabaykamarezv.model.Jobs
import com.mert.arabaykamarezv.view.AdminJobActivity

class JobAdapter (val Joblist : ArrayList<Jobs>) : RecyclerView.Adapter<JobAdapter.JobHolder>(){
    class JobHolder (val binding : RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobHolder {
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return JobHolder(binding)
    }

    override fun onBindViewHolder(holder: JobHolder, position: Int) {
        holder.binding.recyclerViewTextView.text = Joblist.get(position).name
        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView.context, AdminJobActivity::class.java)
            intent.putExtra("info" ,"old")
            intent.putExtra("id",Joblist[position].id)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return Joblist.size
    }
}