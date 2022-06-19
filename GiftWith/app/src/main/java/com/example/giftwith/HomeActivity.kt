package com.example.giftwith

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.giftwith.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db:FirebaseFirestore
    lateinit var username:String
    lateinit var gender:String
    lateinit var birthday:String
    lateinit var uploadedUrl:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        setContentView(view)
        getData()
        hideButton()
    }
    private fun hideButton() {
        val currentUser = auth.currentUser
        if (currentUser != null){
        }
    }
    private fun getData() {
        val docRef = db.collection("Profiles")
        val currentUser = auth.currentUser
        val userUid = currentUser?.uid!!
        currentUser?.let {
            docRef.get().addOnCompleteListener {
                if (it.isSuccessful) {
                    for (doc in it.result) {
                        if (userUid == (doc.data.getValue("UID") as String))
                        {
                        username = doc.data.getValue("Username") as String
                        birthday = doc.data.getValue("Gender") as String
                        gender = doc.data.getValue("Birthday") as String
                        uploadedUrl = doc.data.getValue("downloadedUrl") as String
                        }
                    }
                }
                binding.textUsername.setText(username)
                binding.titleGender.setText(gender)
                binding.titleBirthday.setText(birthday)
                Picasso.get().load(uploadedUrl).into(binding.profilePhotoUser);
            }
        }
    }

    private fun sendData(type:String) {
        val docRef = db.collection("Profiles")
        val currentUser = auth.currentUser
        val userUid = currentUser?.uid!!
        val intent = Intent(this@HomeActivity,ItemListActivity::class.java)
        currentUser?.let {
            docRef.get().addOnCompleteListener {
                if (it.isSuccessful) {
                    for (doc in it.result) {
                        val id = (doc.data.getValue("UID") as String)
                        if (userUid == id)
                        {
                            username = doc.data.getValue("Username") as String
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

    fun showRecommendations(view: View)
    {
        Toast.makeText(this,"Recom",Toast.LENGTH_LONG).show()
        sendData("Recommendation")
    }
    fun showWishes(view: View){
        /*
        val docRef = db.collection("Profiles")
        val currentUser = auth.currentUser
        val userUid = currentUser?.uid!!
        val intent = Intent(this@HomeActivity,ItemListActivity::class.java)
        currentUser?.let {
            docRef.get().addOnCompleteListener {
                if (it.isSuccessful) {
                    for (doc in it.result) {
                        if (userUid == (doc.data.getValue("UID") as String))
                        {
                            username = doc.data.getValue("Username") as String
                            intent.putExtra("Username", username)
                            intent.putExtra("UID", doc.data.getValue("UID") as String)
                            intent.putExtra("Type", "Wish")
                            println("Gönderilenler $username and ${doc.data.getValue("UID") as String}")
                            startActivity(intent)
                        }
                    }
                }
            }
        }
*/
        Toast.makeText(this,"Wishes",Toast.LENGTH_LONG).show()
        sendData("Wish")
    }

    fun showGroups(view: View){
        val intent = Intent(this@HomeActivity,GroupActivity::class.java)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInf = menuInflater
        menuInf.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.SignOut)
        {
            auth.signOut()
            Toast.makeText(this, "Successfully Signed Out",Toast.LENGTH_LONG).show()
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}