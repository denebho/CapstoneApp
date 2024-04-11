package com.example.ayosapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ayosapp.R
import com.example.ayosapp.data.BookingsData
import com.example.ayosapp.databinding.ItemHomeBinding
import java.text.SimpleDateFormat
import java.util.Locale

class HomeAdapter(
    private val context: Context,
    private val homeList : ArrayList<BookingsData>) :
    RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    inner class HomeViewHolder(val binding: ItemHomeBinding):
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(
            ItemHomeBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val currentItem = homeList[position]
        holder.binding.apply {
            val time = timestampToString(currentItem.timeScheduled)
            when (currentItem.service) {
                "Appliance" -> itemImage.setImageResource(R.drawable.home_appliance)
                "Electrical" -> itemImage.setImageResource(R.drawable.home_electrical)
                "Aircon" -> itemImage.setImageResource(R.drawable.home_aircon)
                "Plumbing" -> itemImage.setImageResource(R.drawable.home_plumbing)
            }
            itemDate.text = time
            statusBar.text = currentItem.status
        }
    }

    override fun getItemCount(): Int {
        return homeList.size
    }

        fun timestampToString(timestamp: com.google.firebase.Timestamp?): String {
            val date = timestamp?.toDate()
            val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
            val dateString = dateFormat.format(date!!)
            return dateString
        }

}