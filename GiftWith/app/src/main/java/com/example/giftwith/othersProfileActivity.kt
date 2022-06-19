package com.example.giftwith

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.giftwith.databinding.ActivityOthersProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class othersProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOthersProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    lateinit var username:String
    lateinit var gender:String
    lateinit var birthday:String
    lateinit var uploadedUrl:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOthersProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val inIntent = intent
        val name = inIntent.getStringExtra("memberPos")
        binding.tName.setText(name)
        getData(name!!)
    }

    private fun sendData(type:String,name:String) {
        val docRef = db.collection("Profiles")
        val currentUser = auth.currentUser
        val intent = Intent(this,ItemListActivity::class.java)
        currentUser?.let {
            docRef.get().addOnCompleteListener {
                if (it.isSuccessful) {
                    for (doc in it.result) {
                        val id = (doc.data.getValue("UID") as String)
                        val username = doc.data.getValue("Username") as String
                        if (name == username)
                        {
                            intent.putExtra("Username", username)
                            intent.putExtra("UID", id)
                            intent.putExtra("Type", type)
                            println("Gönderilenler $username and ${id} and $type")
                            startActivity(intent)
                        }
                    }
                }
            }
        }
    }

    private fun getData(name:String){
        val docRef = db.collection("Profiles")
        docRef.get().addOnCompleteListener {
            if (it.isSuccessful) {
                for (doc in it.result) {
                    if (name == (doc.data.getValue("Username") as String))
                        {
                                username = doc.data.getValue("Username") as String
                                birthday = doc.data.getValue("Gender") as String
                                gender = doc.data.getValue("Birthday") as String
                                uploadedUrl = doc.data.getValue("downloadedUrl") as String

                        }
                    }
                }
                binding.textGenderOthers.setText(gender)
                binding.BirthdayOthers.setText(birthday)
                Picasso.get().load(uploadedUrl).into(binding.otherPP);

        }
    }
    fun showWishes(view: View){
        val inIntent = intent
        val name = inIntent.getStringExtra("memberPos")
        Toast.makeText(this,"Wishes",Toast.LENGTH_LONG).show()
        sendData("Wish", name!!)
    }
    fun showRecommendations(view: View){
        val inIntent = intent
        val name = inIntent.getStringExtra("memberPos")
        sendData("Recommendation", name!!)
        Toast.makeText(this,"Recom", Toast.LENGTH_LONG).show()
    }

}


