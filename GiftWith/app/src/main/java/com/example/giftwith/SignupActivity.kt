package com.example.giftwith

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.giftwith.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        var view = binding.root
        setContentView(view)
        auth = FirebaseAuth.getInstance()

    }
    fun signUpfun(view: View) {
        val email = binding.textEmailAddressS.text.toString()
        val password = binding.textPasswordS.text.toString()
        if(email.isEmpty() || password.isEmpty())
        {
            Toast.makeText(this@SignupActivity,"Please fill empty fields", Toast.LENGTH_LONG).show()
        }
        else {
            auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener {
                val intent = Intent(this@SignupActivity, ProfileinfoActivity::class.java)
                startActivity(intent)
                finish()
                Toast.makeText(this@SignupActivity,"SignUp Successfully Terminated",Toast.LENGTH_LONG).show()
            }
                .addOnFailureListener {
                    Toast.makeText(this@SignupActivity,it.localizedMessage,Toast.LENGTH_LONG).show()
                }
        }

    }
}