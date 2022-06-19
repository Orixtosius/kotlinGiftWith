package com.example.giftwith

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.giftwith.databinding.ActivityEditGroupBinding
import com.example.giftwith.databinding.ActivityIngroupBinding
import com.example.giftwith.model.EditGroupAdapter
import com.example.giftwith.model.InGroupAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Exception

class IngroupActivity : AppCompatActivity() {
    lateinit var binding: ActivityIngroupBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIngroupBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        getMember()

    }

    fun editGroup(view: View){
        val intentToEdit = Intent(this@IngroupActivity, EditGroupActivity::class.java)
        try {
            getMember1()
        }
        catch (e: Exception){
            println("Hata ${e.localizedMessage}")
        }
    }

    fun getMember(){
        val InIntent = intent
        val groupId = InIntent.getStringExtra("id")
        var admin: String = ""

        val groupName = InIntent.getStringExtra("groupName")
        println("Group name $groupName")
        val IntenttoEdit = Intent(this@IngroupActivity, EditGroupActivity::class.java)
        IntenttoEdit.putExtra("GroupName",groupName)
        binding.groupName.setText(groupName)

        val membersId : ArrayList<ArrayList<String>> = ArrayList()
        val memberNames : ArrayList<String> = ArrayList()

        var dr = db.collection("Groups")
        var docRef = db.collection("Profiles")
        dr.get().addOnCompleteListener {
            if (it.isSuccessful) {
                for (doc in it.result) {
                    if (doc.id == groupId) {
                        val value = doc.data.getValue("users") as ArrayList<String>
                        val groupId = doc.id
                        admin = doc.data.getValue("admin") as String
                        membersId.add(value)
                        IntenttoEdit.putExtra("GID",groupId)
                        println("Sending $groupId")

                    }
                }
                docRef.get().addOnCompleteListener {
                    if (it.isSuccessful) {
                        for (doc in it.result) {
                            val value = doc.data.getValue("UID")
                            var i = 0
                            var size = membersId.size
                            while(i<size) {
                                if (value in membersId.get(i)) {
                                    val name = doc.data.getValue("Username") as String
                                    memberNames.add(name)
                                }
                                i += 1
                            }
                            binding.recyclerView.layoutManager = LinearLayoutManager(this)

                            if(admin == auth.currentUser?.uid){
                                println("H")
                                binding.editButton.visibility = View.VISIBLE

                            }
                            val adapter = InGroupAdapter(memberNames)
                            binding.recyclerView.adapter = adapter


                        }

                    }
                }



            }
        }

    }

    fun getMember1(){
        val InIntent = intent
        val groupId = InIntent.getStringExtra("id")
        var admin: String = ""

        val groupName = InIntent.getStringExtra("groupName")
        println("Group name $groupName")
        val IntenttoEdit = Intent(this@IngroupActivity, EditGroupActivity::class.java)
        IntenttoEdit.putExtra("GroupName",groupName)
        binding.groupName.setText(groupName)

        val membersId : ArrayList<ArrayList<String>> = ArrayList()
        val memberNames : ArrayList<String> = ArrayList()

        var dr = db.collection("Groups")
        var docRef = db.collection("Profiles")
        dr.get().addOnCompleteListener {
            if (it.isSuccessful) {
                for (doc in it.result) {
                    if (doc.id == groupId) {
                        val value = doc.data.getValue("users") as ArrayList<String>
                        val groupId = doc.id
                        admin = doc.data.getValue("admin") as String
                        membersId.add(value)
                        IntenttoEdit.putExtra("GID",groupId)

                    }
                }
                docRef.get().addOnCompleteListener {
                    if (it.isSuccessful) {
                        for (doc in it.result) {
                            val value = doc.data.getValue("UID")
                            var i = 0
                            var size = membersId.size
                            while(i<size) {
                                if (value in membersId.get(i)) {
                                    val name = doc.data.getValue("Username") as String
                                    memberNames.add(name)
                                }
                                i += 1
                            }
                            binding.recyclerView.layoutManager = LinearLayoutManager(this)

                            if(admin == auth.currentUser?.uid){
                                binding.editButton.visibility = View.VISIBLE

                            }
                            val adapter = InGroupAdapter(memberNames)
                            binding.recyclerView.adapter = adapter
                            adapter.notifyDataSetChanged()

                        }

                    }
                }


                startActivity(IntenttoEdit)
            }
        }

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