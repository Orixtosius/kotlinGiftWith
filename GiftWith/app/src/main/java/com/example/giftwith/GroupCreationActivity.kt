package com.example.giftwith

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.giftwith.databinding.ActivityGroupCreationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.util.*

class GroupCreationActivity : AppCompatActivity() {
    lateinit var binding: ActivityGroupCreationBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupCreationBinding.inflate(layoutInflater)
        val view = binding.root
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        setContentView(view)

    }

    fun cg(view: View) {
        val groupLink = UUID.randomUUID().toString()
        val groupName = binding.textGroupName.text.toString()
        val docRef = db.collection("Groups").document(groupLink)
        val currentUser = auth.currentUser
        val admin = currentUser?.uid!!
//        val Map = hashMapOf<String,Any>()
 //           if (currentUser != null && groupName.isNotEmpty()) {
 //               Map.put("groupName", groupName)
 /*               db.collection("Groups").add(Map).addOnSuccessListener {
                    Toast.makeText(this,"Success",Toast.LENGTH_LONG).show()
                    println("Doc id > ${docRef.id}")
                    println("Doc ZEL id > ${docRef.id[-1]}")
                }
                val washingtonRef = db.collection("cities").document("DC")

// Atomically add a new region to the "regions" array field.
washingtonRef.update("regions", FieldValue.arrayUnion("greater_virginia"))

*/          if (currentUser != null && groupName.isNotEmpty()){
            val data = hashMapOf("groupName" to groupName,"admin" to admin)
            docRef.set(data, SetOptions.merge()).addOnSuccessListener {
                   docRef.update("users",FieldValue.arrayUnion(currentUser.uid))
                    Toast.makeText(this,"Success",Toast.LENGTH_LONG).show()
                    val intent = Intent(this, GroupActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            else {
                Toast.makeText(this, "Please insert a name for your group", Toast.LENGTH_LONG).show()
            }

        }
    }
