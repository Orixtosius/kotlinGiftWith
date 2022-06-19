package com.example.giftwith

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.giftwith.databinding.ActivityGroupHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class GroupHomeActivity : AppCompatActivity() {
    lateinit var binding:ActivityGroupHomeBinding
    lateinit var auth:FirebaseAuth
    lateinit var db:FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
    fun joinGroup(view: View) {
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        var link = binding.groupLink.text.toString()
        var docRef = db.collection("Groups")
        var currentUser = auth.currentUser
        var control: Int = 0

        currentUser?.let {
            docRef.get().addOnCompleteListener {
                if (it.isSuccessful) {
                    for (doc in it.result) {
                        if(doc.id == link)
                        {
                            Toast.makeText(this, "We are directing into the Groups Page", Toast.LENGTH_LONG).show()
                            control = 1
                            addUser(doc.id, currentUser?.uid!!)
                            break
                        }
                    }
                    if(control == 0){
                        Toast.makeText(this, "We could not find such groups.\n" +
                                "Please, check the provided link", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this, it.exception?.localizedMessage, Toast.LENGTH_LONG).show()
                }
            }

        }


    }

    fun addUser(groupId:String,id:String){

        val docRef = db.collection("Groups").document(groupId)
        docRef.update("users",FieldValue.arrayUnion(id))
        val intent = Intent(this,GroupActivity::class.java)
        startActivity(intent)
        finish()
    }


}