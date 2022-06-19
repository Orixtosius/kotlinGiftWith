package com.example.giftwith

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.giftwith.databinding.ActivityGroupBinding
import com.example.giftwith.model.GNamesAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class GroupActivity : AppCompatActivity() {
    lateinit var binding: ActivityGroupBinding
    lateinit var db: FirebaseFirestore
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val uid = currentUser?.uid!!

        val groupNames = ArrayList<String>()
        var dr = db.collection("Groups")
        dr.get().addOnCompleteListener {
            if (it.isSuccessful) {
                for (doc in it.result) {
                    val membersId = doc.data.getValue("users") as ArrayList<String>
                    if (uid in membersId) {
                        val value = doc.data.getValue("groupName") as String
                        groupNames.add(value)
                        binding.rV.layoutManager = LinearLayoutManager(this)
                        val adapter = GNamesAdapter(groupNames)
                        binding.rV.adapter = adapter
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        }

    }


    fun createGrup(view: View){
        val intent = Intent(this,GroupCreationActivity::class.java)
        startActivity(intent)
    }

    fun selectGrup(view: View){
    //val nameofButton = binding.buttonGroup1.text.toString()
        val nameofButton = "Liseliler"
        val intent = Intent(this,IngroupActivity::class.java)
        intent.putExtra("GroupName",nameofButton)
        startActivity(intent)
    }

    fun joinGrup(view: View){
        val intent = Intent(this,GroupHomeActivity::class.java)
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
            Toast.makeText(this, "Successfully Signed Out", Toast.LENGTH_LONG).show()
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}