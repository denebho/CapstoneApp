package com.example.ayosapp.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ayosapp.ayosPackage.AyosGetLocationFragment
import com.example.ayosapp.ayosPackage.AyosMap
import com.example.ayosapp.data.AddressData
import com.example.ayosapp.databinding.ItemAddressBinding


class AddressAdapter(
    private val dataArrayList: ArrayList<AddressData>,
    private val context: Context
    //,private val itemClickListener: OnItemClickListener
):
        RecyclerView.Adapter<AddressAdapter.AddressViewHolder>(){
    private var listener: View.OnClickListener? = null
    inner class AddressViewHolder(val binding: ItemAddressBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

//    class AddressViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
//        val addressLine: TextView = itemView.findViewById(R.id.itemAddress1)
//        val addressDetails: TextView = itemView.findViewById(R.id.itemAddress2)
//        val addressID: String = ""
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        return AddressViewHolder(
            ItemAddressBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun getItemCount(): Int {
        return dataArrayList.size
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        if(dataArrayList.isNotEmpty()) {
            val currentAddress = dataArrayList[position]

            holder.binding.apply {
                itemAddress1.text = dataArrayList[position].address
                itemAddress2.text = dataArrayList[position].address_details
            }
            holder.binding.editBtn.setOnClickListener(){
                val intent = Intent(context, AyosMap::class.java)
                intent.putExtra("address",currentAddress.address)
                intent.putExtra("addressdetails",currentAddress.address_details)
                intent.putExtra("uid",currentAddress.UID)
                intent.putExtra("addressid",currentAddress.addressID)
                intent.putExtra("latitude",currentAddress.latitude)
                intent.putExtra("instructions",currentAddress.instructions)
                intent.putExtra("longitude",currentAddress.longitude)
                context.startActivity(intent)
            }
            holder.binding.addressCard.setOnClickListener(){
//                val action = AddressFragmentDirections.actionMyFragmentToNextFragment(currentAddress.address, currentAddress.addressID)
//                navController.navigate(action)
                val intent = Intent(context, AyosGetLocationFragment::class.java)
                intent.putExtra("address",currentAddress.address)
                intent.putExtra("addressdetails",currentAddress.address_details)
                intent.putExtra("addressid",currentAddress.addressID)
                context.startActivity(intent)
                Log.d("addressadapter debug", "card clicked")
            }



//            holder.addressLine.text = dataArrayList[position].address
//            holder.addressDetails.text = dataArrayList[position].address_details
//            holder.binding.a
//                .setOnClickListener {listener?.onItemClick(item)}
        }

    }
    class MyViewHolder(val binding: ItemAddressBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AddressData) {
            // Bind data to views
        }
    }
}