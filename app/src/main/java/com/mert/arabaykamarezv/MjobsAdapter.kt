package com.mert.arabaykamarezv

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mert.arabaykamarezv.databinding.MusterirecyclerrowBinding
import com.mert.arabaykamarezv.databinding.RecyclerRowBinding

class MjobsAdapter (val MjobList : ArrayList<MüsteriJobs>): RecyclerView.Adapter<MjobsAdapter.MjobsHolder>() {
    class MjobsHolder (val binding: MusterirecyclerrowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MjobsHolder {
        val binding = MusterirecyclerrowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MjobsHolder(binding)
    }

    override fun onBindViewHolder(holder: MjobsHolder, position: Int) {
        holder.binding.MusteriIlanText.text = MjobList.get(position).Plaka
        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView.context,MusteriJobAddActivity::class.java)
            intent.putExtra("info", "old")
            intent.putExtra("id",MjobList[position].id)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return MjobList.size
    }
}