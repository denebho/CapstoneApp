package com.example.ayosapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ayosapp.data.ChargesData
import com.example.ayosapp.databinding.ItemAyosbreakdownBinding

class ChargesAdapter(
    private val context: Context,
    private val chargeList: ArrayList<ChargesData>
) :
    RecyclerView.Adapter<ChargesAdapter.ChargesAdapterViewHolder>() {
    private var dataArrayList = ArrayList<ChargesData>()

    inner class ChargesAdapterViewHolder(val binding: ItemAyosbreakdownBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChargesAdapterViewHolder {
        return ChargesAdapterViewHolder(
            ItemAyosbreakdownBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return chargeList.size
    }

    override fun onBindViewHolder(holder: ChargesAdapterViewHolder, position: Int) {
        val currentItem = chargeList[position]
        holder.binding.apply {
            extraCharge.text = currentItem.charge
            extraFee.text = currentItem.price.toString()
        }
    }
}