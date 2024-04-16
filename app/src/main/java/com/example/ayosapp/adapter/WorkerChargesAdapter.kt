package com.example.ayosapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.ayosapp.R
import com.example.ayosapp.data.ChargesData
import com.example.ayosapp.databinding.ItemAyosbreakdownBinding

class WorkerChargesAdapter(
    private val context: Context,
    private val chargeList: ArrayList<ChargesData>
) :
    RecyclerView.Adapter<WorkerChargesAdapter.WorkerChargesAdapterViewHolder>() {
    private var dataArrayList = ArrayList<ChargesData>()
    inner class WorkerChargesAdapterViewHolder(val binding: ItemAyosbreakdownBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkerChargesAdapterViewHolder {
        return WorkerChargesAdapterViewHolder(
            ItemAyosbreakdownBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: WorkerChargesAdapterViewHolder, position: Int) {
        val currentItem = chargeList[position]
        holder.binding.apply {
            extraCharge.text = currentItem.charge
            extraFee.text = currentItem.price.toString()
            removeCharge.setImageResource(R.drawable.baseline_delete_24)
        }
        holder.binding.removeCharge.setOnClickListener {
            deleteDialog(currentItem.charge,position)
        }
    }

    override fun getItemCount(): Int {
        return chargeList.size
    }
    private fun deleteItem(index: Int){
        chargeList.removeAt(index)
        notifyItemRemoved(index)
    }
    private fun getTotal(): Double {
        var total = 0.0
        for (chargesData in dataArrayList) {
            total += chargesData.price ?: 0.0
        }
        return total
    }
    private fun deleteDialog(charge: String?, position: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Continue deleting address?")
        builder.setMessage("Are you sure you want to remove \n$charge")
        builder.setPositiveButton("YES") { _, _ ->
            deleteItem(position)
        }
        builder.setNegativeButton("NO") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

}