package com.example.ayosapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ayosapp.data.BookingsData
import com.google.android.material.imageview.ShapeableImageView

class HomeAdapter(private val homeList : ArrayList<BookingsData>) :
    RecyclerView.Adapter<HomeAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_home,
            parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = homeList[position]
        holder.homeImage.setImageResource(currentItem.image)
        holder.homeStatus.text = currentItem.status
        holder.homeDate.text = currentItem.dateofService
    }

    override fun getItemCount(): Int {
        return homeList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val homeImage : ShapeableImageView = itemView.findViewById(R.id.itemImage)
        val homeStatus : TextView = itemView.findViewById(R.id.statusBar)
        val homeDate : TextView = itemView.findViewById(R.id.itemDate)
    }

}