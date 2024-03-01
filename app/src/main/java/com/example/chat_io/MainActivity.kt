package com.example.chat_io

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.chat_io.SecondActivity
import com.example.chat_io.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        mAuth= FirebaseAuth.getInstance()
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.signup.setOnClickListener {
            intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }
        binding.text1.setOnClickListener{
            val email = binding.tex1.text.toString()
            val password = binding.tex2.text.toString()
            Login(email, password)
        }
    }
    private fun Login(email:String, password:String){
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    intent = Intent(this, chatscreen::class.java)
                    finish()
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "User does not exist", Toast.LENGTH_SHORT).show()
                }
            }
    }
}