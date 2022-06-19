package com.example.giftwith

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.giftwith.databinding.ActivityItemListBinding
import com.example.giftwith.model.InGroupAdapter
import com.example.giftwith.model.ItemListAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import java.util.*
import javax.annotation.Nullable
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ItemListActivity : AppCompatActivity() {
    private lateinit var binding : ActivityItemListBinding
    private var db = FirebaseFirestore.getInstance()
    private var auth = FirebaseAuth.getInstance()
    private val currentUser = auth.currentUser
    private var admin = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val inIntent = intent
        val username = inIntent.getStringExtra("Username")
        val UID = inIntent.getStringExtra("UID")
        val type = inIntent.getStringExtra("Type")
        println("Gelenler ${UID},$type,$username")

        if(type == "Recommendation"){
            if(UID == currentUser?.uid){
                binding.itemText.visibility = View.INVISIBLE
                binding.buttonAddItem.visibility = View.INVISIBLE
            }
            else{
                binding.itemText.visibility = View.VISIBLE
                binding.buttonAddItem.visibility = View.VISIBLE
            }
        }
        else{
            if(UID == currentUser?.uid){
                binding.itemText.visibility = View.VISIBLE
                binding.buttonAddItem.visibility = View.VISIBLE

            }
            else{
                binding.itemText.visibility = View.INVISIBLE
                binding.buttonAddItem.visibility = View.INVISIBLE
            }
        }

        getData(username!!, UID!!,type!!)
    }


    fun addItem(view: View){
        val itemName = binding.itemText.text.toString()
        val inIntent = intent
        val UID = inIntent.getStringExtra("UID")
        val type = inIntent.getStringExtra("Type")
        val docL = db.collection("List2")

        if (currentUser != null) {
            if (itemName.isNotEmpty()) {
                    docL.whereEqualTo("listType", type).whereEqualTo("userID", UID).get()
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                for (doc in it.result) {
                                    println("Exists")
                                    docL.document(doc.id)
                                        .update("items", FieldValue.arrayUnion(itemName))
                                    Toast.makeText(
                                        this,
                                        "Success.\nItem has been added",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                    if(it.result.isEmpty){
                                        val listMap = hashMapOf<String, Any>()
                                        val item = ArrayList<String>()
                                        item.add(itemName)
                                        listMap.put("listType", type!!)
                                        listMap.put("userID", UID!!)
                                        listMap.put("items", item)
                                        println("works until here")
                                        db.collection("List2").add(listMap)
                                    }

                            }
                        }.addOnFailureListener {
                            Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
                            println("hata")
                        }

            }
            else{
                Toast.makeText(this, "You have not written anything", Toast.LENGTH_LONG).show()
            }
        }

        else{
            if(currentUser == null){
                Toast.makeText(this,"Authorization Error",Toast.LENGTH_LONG).show()
            }
            else{
                Toast.makeText(this,"Item Field Is Empty\nPlease Put A Product Name",Toast.LENGTH_LONG).show()
            }
        }

    }

    fun getData(username:String, UID:String, type: String){
        val docRef = db.collection("List2")
        docRef.whereEqualTo("userID", UID).addSnapshotListener { value, error ->
                if(error == null)
                {
                    if(value != null){
                        var itemCodes: ArrayList<String>
                        for(doc in value){
                            if(type == doc.data.getValue("listType") as String){
                                itemCodes = doc.data.getValue("items") as ArrayList<String>
                                binding.listNameTextView.text = type
                                if(UID == currentUser?.uid){
                                    admin = true
                                }
                                if(admin){
                                    if(type == "Wish"){
                                        binding.itemText.visibility = View.VISIBLE
                                        binding.buttonAddItem.visibility = View.VISIBLE
                                    }
                                    else{
                                        binding.itemText.visibility = View.INVISIBLE
                                        binding.buttonAddItem.visibility = View.INVISIBLE
                                    }
                                }
                                else{
                                    if(type == "Wish"){
                                        binding.itemText.visibility = View.INVISIBLE
                                        binding.buttonAddItem.visibility = View.INVISIBLE
                                    }
                                    else{
                                        binding.itemText.visibility = View.VISIBLE
                                        binding.buttonAddItem.visibility = View.VISIBLE
                                    }
                                }
                                db.collection("Profiles").whereEqualTo("UID",UID).get().addOnCompleteListener {
                                    if(it.isSuccessful){
                                        for(doc in it.result){
                                                binding.textViewUserName.text = doc.data.getValue("Username") as String

                                        }
                                    }
                                    binding.recItemView.layoutManager = LinearLayoutManager(this)
                                    val adapter = ItemListAdapter(itemCodes,admin,type)
                                    binding.recItemView.adapter = adapter
                                }
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