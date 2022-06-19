package com.example.giftwith

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.giftwith.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class loginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth = FirebaseAuth.getInstance()
    }
    fun signIn(view: View) {
        val email = binding.textEmailAddress.text.toString()
        val password = binding.textPassword.text.toString()
        if(email.isEmpty() || password.isEmpty())
        {
            Toast.makeText(this@loginActivity,"Please fill empty fields",Toast.LENGTH_LONG).show()
        }
        else {
            val email = binding.textEmailAddress.text.toString()
            val password = binding.textPassword.text.toString()
            auth.signInWithEmailAndPassword(email,password).addOnSuccessListener {
                val intent = Intent(this@loginActivity, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
                .addOnFailureListener {
                    Toast.makeText(this@loginActivity,it.localizedMessage,Toast.LENGTH_LONG).show()
                }
        }

    }
}