package com.example.ayosapp.adapter

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.ayosapp.ayosPackage.AyosBookingActivity
import com.example.ayosapp.ayosPackage.AyosMap
import com.example.ayosapp.data.AddressData
import com.example.ayosapp.databinding.ItemAddressBinding
import com.google.firebase.firestore.FirebaseFirestore


class AddressAdapter(
    private val dataArrayList: ArrayList<AddressData>,
    private val context: Context,
    private val bundle: String,
    private val firestore: FirebaseFirestore
):
        RecyclerView.Adapter<AddressAdapter.AddressViewHolder>(){
    inner class AddressViewHolder(val binding: ItemAddressBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        return AddressViewHolder(
            ItemAddressBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun getItemCount(): Int {
        return dataArrayList.size
    }
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        if(dataArrayList.isNotEmpty()) {
            val currentAddress = dataArrayList[position]

            holder.binding.apply {
                itemAddress1.text = dataArrayList[position].address
                itemAddress2.text = dataArrayList[position].address_details
            }
            holder.binding.editBtn.setOnClickListener{
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
            holder.binding.deleteBtn.setOnClickListener{
                deleteDialog(currentAddress.addressID, currentAddress.address)
            }
            holder.binding.addressCard.setOnClickListener{
                val intent = Intent(context, AyosBookingActivity::class.java)
                intent.putExtra("addressline",currentAddress.address)
                intent.putExtra("addressid",currentAddress.addressID)
                intent.putExtra("serviceCode", bundle)
                context.startActivity(intent)
                Log.d("addressadapter debug", "card clicked")
            }

        }

    }
    private fun deleteItemFromFirestore(documentId: String?) {
        val docRef = firestore.collection("address").document(documentId.toString())
        docRef.delete()
            .addOnSuccessListener {
                notifyDataSetChanged()
                Log.d(TAG, "DocumentSnapshot successfully deleted!")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error deleting document", e)
            }
    }
    private fun deleteDialog(documentId: String?, addressLine: String?) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Continue deleting address?")
        builder.setMessage("Are you sure you want to delete the following address? \n$addressLine")
        builder.setPositiveButton("YES") { _, _ ->
            deleteItemFromFirestore(documentId)
        }
        builder.setNegativeButton("NO") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

}