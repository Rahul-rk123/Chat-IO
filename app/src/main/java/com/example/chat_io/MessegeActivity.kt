package com.example.chat_io

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MessegeActivity : AppCompatActivity() {
    var senderRoom:String? = null
    var receiverRoom:String? = null
    private lateinit var messageview:RecyclerView
    private lateinit var messagebox:EditText
    private lateinit var sendbutton:ImageView
    private lateinit var messageAdapter: messageAdapter
    private lateinit var messagelist:ArrayList<messages>
    private lateinit var mdbref:DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messege)
        val name = intent.getStringExtra("name")
        val receiveruid = intent.getStringExtra("uid")
        val senderuid = FirebaseAuth.getInstance().currentUser?.uid
        mdbref = FirebaseDatabase.getInstance().getReference()
        senderRoom = receiveruid+senderuid
        receiverRoom = senderuid+receiveruid
        supportActionBar?.title = name
        messageview = findViewById(R.id.messageview)
        messagebox = findViewById(R.id.messagebox)
        sendbutton = findViewById(R.id.sendview)
        messagelist = ArrayList()
        messageAdapter = messageAdapter(this, messagelist)
        messageview.layoutManager = LinearLayoutManager(this)
        messageview.adapter = messageAdapter
        // Adding data to recycler view
        mdbref.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    messagelist.clear()
                    for(postsnapshot in snapshot.children){
                        val messages = postsnapshot.getValue(messages::class.java)
                        messagelist.add(messages!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
        //adding chats to database
        sendbutton.setOnClickListener {
            val message = messagebox.text.toString()
            val messageobject = messages(message, senderuid)
            mdbref.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageobject).addOnSuccessListener {
                    mdbref.child("chats").child(receiverRoom!!).child("messages").push()
                        .setValue(messageobject)
                }
            messagebox.setText("")
        }
    }
}