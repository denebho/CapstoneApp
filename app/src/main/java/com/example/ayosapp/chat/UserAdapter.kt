package com.example.ayosapp.chat

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ayosapp.FillerActivity
import com.example.ayosapp.databinding.ItemUserLayoutBinding
import java.text.SimpleDateFormat
import java.util.Date

class UserAdapter(
    private val userList: ArrayList<User>,
    val context: Context
):
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    inner class UserViewHolder(val binding: ItemUserLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder (
            ItemUserLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun getItemCount(): Int {
        return  userList.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val User = userList[position]

        holder.binding.apply {
            txtName.text = User.name
            chatMessagePreview.text = User.last_message
            val timestamp = User.time
            val date: Date? = timestamp?.toDate()
            val sdf = SimpleDateFormat("HH:mm")
            val timestr = sdf.format(date)
            chatMessageTime.text = timestr
        }
        holder.itemView.setOnClickListener{
            val intent = Intent(context,FillerActivity::class.java)
            intent.putExtra("name", User.name)
            intent.putExtra("id", User.inbox_id)
            intent.putExtra("workerid", User.worker_id)
            intent.putExtra("fragmentTag", "chatFragment")
            context.startActivity(intent)
        }
    }
//    class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
//        val textName = itemView.findViewById<TextView>(R.id.txt_name)
//    }
}