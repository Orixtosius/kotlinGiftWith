package com.example.giftwith

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.giftwith.databinding.ActivityChangeGroupNameBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ChangeGroupNameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangeGroupNameBinding
    private var db = FirebaseFirestore.getInstance()
    private var auth = FirebaseAuth.getInstance()
    private val inIntent = intent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeGroupNameBinding.inflate(layoutInflater)
        val view = binding.root
        val oldGroupName = inIntent.getStringExtra("GNAME") as String
        binding.oldName.text = oldGroupName
        setContentView(view)
    }
    fun changeName(view: View){
        val newGroupName = binding.newGroupName.text
        val groupID = inIntent.getStringExtra("GID") as String
        val docRef = db.collection("Groups").document(groupID)
        docRef.update("groupName", "newGroupName")
        val intent = Intent(this,EditGroupActivity::class.java)
        startActivity(intent)
        finish()
    }
}