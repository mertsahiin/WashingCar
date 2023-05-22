package com.mert.arabaykamarezv.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.Placeholder
import androidx.recyclerview.widget.RecyclerView
import com.mert.arabaykamarezv.databinding.RecyclerRowKonumBinding
import com.mert.arabaykamarezv.model.Place
import com.mert.arabaykamarezv.view.ArabaKonumActivity

class PlaceAdapter(val placeList : List<Place>) : RecyclerView.Adapter<PlaceAdapter.PlaceHolder>() {
    class PlaceHolder(val recyclerRowKonumBinding: RecyclerRowKonumBinding) : RecyclerView.ViewHolder(recyclerRowKonumBinding.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceHolder {
       val recyclerRowKonumBinding = RecyclerRowKonumBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PlaceHolder(recyclerRowKonumBinding)
    }

    override fun onBindViewHolder(holder: PlaceHolder, position: Int) {
       holder.recyclerRowKonumBinding.recyclerViewTextViewKonum.text = placeList.get(position).plaka
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context,ArabaKonumActivity::class.java)
            intent.putExtra("selectedPlace",placeList.get(position))
            intent.putExtra("info","old")
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
       return placeList.size
    }

}