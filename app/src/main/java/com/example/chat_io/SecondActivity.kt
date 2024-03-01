package com.example.chat_io

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.chat_io.databinding.ActivitySecondBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class SecondActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySecondBinding
    private lateinit var auth:FirebaseAuth
    private lateinit var mdbref:DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_second)
        auth = FirebaseAuth.getInstance()
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.signup1.setOnClickListener {
            val name = binding.tett1.text.toString()
            val email = binding.tett2.text.toString()
            val password = binding.tett3.text.toString()
            signup(name, email, password)
        }
    }
    private fun signup(name:String, email:String, password:String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    addUserDatabase(name, email, auth.currentUser?.uid!!)
                    intent = Intent(this, chatscreen::class.java)
                    finish()
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Please enter correct details", Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun addUserDatabase(name:String, email: String, uid:String){
        mdbref = FirebaseDatabase.getInstance().reference
        mdbref.child("user").child(uid).setValue(User(name, email, uid))
    }

}
