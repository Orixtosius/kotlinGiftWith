package com.example.giftwith

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.giftwith.databinding.ActivityHomeBinding
import com.example.giftwith.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser != null)
        {
            val intent = Intent(this@MainActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    fun creationStarter(view: View) {
        intent = Intent(this@MainActivity, SignupActivity::class.java)
        startActivity(intent)
    }
    fun loginNavigator(view: View) {
        intent = Intent(this@MainActivity, loginActivity::class.java)
        startActivity(intent)
    }
}