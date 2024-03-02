package com.example.ayosapp.chat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ayosapp.R
import com.example.ayosapp.databinding.FragmentChatListBinding
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FirebaseFirestore


class ChatListFragment : Fragment() {
    private lateinit var binding: FragmentChatListBinding
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userList: ArrayList<User>
    private lateinit var adapter: UserAdapter
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatListBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //super.onViewCreated(view, savedInstanceState)
        userRecyclerView = view.findViewById(R.id.userRecyclerView)
        val layoutManager = LinearLayoutManager(requireActivity())
        userRecyclerView.layoutManager = layoutManager
        userList = arrayListOf()


        val uid = FirebaseAuth.getInstance().uid
        val db = FirebaseFirestore.getInstance()
        val inboxRef = db.collection("inbox_participants")
        inboxRef.whereEqualTo("user_id", uid).get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    //gets inbox and worker ID to pass it
                    val inboxID = document.getString("inbox_id")
                    val workerID = document.getString("worker_id")

                    if (inboxID != null) {
                        // Use the retrieved inboxID to query the inbox collection
                        db.collection("inbox")
                            .document(inboxID)
                            .get()
                            .addOnSuccessListener { inboxDocument ->
                                // Process the retrieved document from the inbox collection
                                val data = inboxDocument.data
                                // Do something with the data
                                val inboxID = data?.get("id") as String?
                                val name = data?.get("name") as String?
                                val message = data?.get("last_message") as String?
                                val timestamp  = data?.get("last_message_sent_time") as Timestamp
                                val inboxData =  User(inboxID, workerID, name, message, timestamp)
                                userList.add(inboxData)
                                Log.w("Chat Fragment", "{$message}")
                                adapter = UserAdapter(userList, requireActivity())
                                userRecyclerView.adapter = adapter
                            }
                            .addOnFailureListener { e ->
                                Log.w("Chat Fragment", "Error getting inbox document", e)
                            }

                    }
                }
            }
            .addOnFailureListener { e ->
                Log.w("Chat Fragment", "Error getting documents from inbox_participants", e)
            }

    }

}