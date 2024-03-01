package com.example.chat_io

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chat_io.User
import com.example.chat_io.userAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class chatscreen : AppCompatActivity() {
    private lateinit var recyclerview:RecyclerView
    private lateinit var userlist:ArrayList<User>
    private lateinit var adapter: userAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var mdbref: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatscreen)

        auth = FirebaseAuth.getInstance()
        mdbref = FirebaseDatabase.getInstance().getReference()
        userlist = ArrayList()
        adapter = userAdapter(this, userlist)
        recyclerview = findViewById(R.id.recyclerview)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = adapter
        mdbref.child("user").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                userlist.clear()
                for (postSnapshot in snapshot.children){
                    val currentUser = postSnapshot.getValue(User::class.java)
                    if(auth.currentUser?.uid!=currentUser?.uid){
                        userlist.add(currentUser!!)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.logout){
            auth.signOut()
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            return true
        }
        return true
    }
}