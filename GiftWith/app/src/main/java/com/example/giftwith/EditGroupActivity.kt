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
import com.example.giftwith.model.EditGroupAdapter
import com.example.giftwith.model.InGroupAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EditGroupActivity : AppCompatActivity() {
    lateinit var binding: ActivityEditGroupBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditGroupBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        val InIntent = intent
        val groupId = InIntent.getStringExtra("GID")
        val groupName = InIntent.getStringExtra("GroupName")
        getData(groupName,groupId)

    }

    fun copyCode(view: View){
        val InIntent = intent
        val groupId = InIntent.getStringExtra("GID")
        val intent = Intent(this, CopyCodeActivity::class.java)
        intent.putExtra("GID",groupId)
        startActivity(intent)
    }
    fun changeName(view: View){
        val InIntent = intent
        val groupId = InIntent.getStringExtra("GID")
        val groupName = InIntent.getStringExtra("GroupName")
        val intent = Intent(this, ChangeGroupNameActivity::class.java)
        intent.putExtra("GID",groupId)
        intent.putExtra("GNAME",groupName)
        startActivity(intent)
    }
    fun deleteGroup(view: View){
        val InIntent = intent
        val groupId = InIntent.getStringExtra("GID")
        db.collection("Groups").document(groupId!!).delete().addOnSuccessListener {
            Toast.makeText(this, "Group successfully deleted",Toast.LENGTH_LONG).show()
        }
        val intent = Intent(this,GroupActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun getData(groupName:String?,groupId:String?){

        binding.groupNameEdit.setText(groupName)
        println("Data has arrived ? ${groupId} and $groupName")
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

                        membersId.add(value)

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
                            binding.recyclerView2.layoutManager = LinearLayoutManager(this)
                            val adapter = EditGroupAdapter(memberNames,groupId!!)
                            binding.recyclerView2.adapter = adapter
                            adapter.notifyDataSetChanged()


                        }
                    }
                }

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
            Toast.makeText(this, "Successfully Signed Out",Toast.LENGTH_LONG).show()
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}