package com.example.hujan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hujan.R
import com.example.hujan.response.TimesItem


class CuacaAdapter(private val cuacalist: List<TimesItem?>?) : RecyclerView.Adapter<CuacaAdapter.CuacaViewHolder>() {

    class CuacaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvJamPerhari: TextView = itemView.findViewById(R.id.tv_jamperhari)
        val tvSuhuPerhari: TextView = itemView.findViewById(R.id.tv_suhuperhari)
        val iv_cuacaperjam: ImageView = itemView.findViewById(R.id.iv_cuacaperjam)
        // Tambahkan elemen lainnya sesuai kebutuhan
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CuacaViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.itemcuaca_perjam, parent, false)
        return CuacaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CuacaViewHolder, position: Int) {
        val currentcuaca = cuacalist!![position]

        holder.tvJamPerhari.text = currentcuaca!!.h
        holder.tvSuhuPerhari.text = currentcuaca!!.celcius

        when (currentcuaca!!.h) {
            "0" -> holder.iv_cuacaperjam.setImageResource(R.drawable.cloudy)
            "6" -> holder.iv_cuacaperjam.setImageResource(R.drawable.cloudy_sunny)
            "12" -> holder.iv_cuacaperjam.setImageResource(R.drawable.rainy)
            "18" -> holder.iv_cuacaperjam.setImageResource(R.drawable.cloudy)
            "24" -> holder.iv_cuacaperjam.setImageResource(R.drawable.cloudy)
        }

    }

    override fun getItemCount(): Int {
        return cuacalist!!.size
    }
}