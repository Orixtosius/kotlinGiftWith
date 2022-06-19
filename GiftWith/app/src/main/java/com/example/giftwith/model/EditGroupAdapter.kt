package com.example.giftwith.model

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.giftwith.EditGroupActivity
import com.example.giftwith.HomeActivity
import com.example.giftwith.databinding.RecyclerRowBinding
import com.example.giftwith.othersProfileActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class EditGroupAdapter(val memberNames : ArrayList<String>,val groupId:String):
    RecyclerView.Adapter<EditGroupAdapter.memberHolder>() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val userUid = currentUser?.uid!!
    private var userId = ""
    private var username: String = ""

    class memberHolder(val binding: RecyclerRowBinding): RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): memberHolder {
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return memberHolder(binding)
    }

    override fun onBindViewHolder(holder: memberHolder, position: Int) {
        holder.binding.groupnames.text = memberNames.get(position)
        val currentUserId = auth.currentUser?.uid
        holder.itemView.setOnClickListener {
            val selectedUserName = memberNames.get(position)
            val docRef = db.collection("Profiles")
            val drG = db.collection("Groups")
            currentUser?.let {
                docRef.get().addOnCompleteListener {
                    if (it.isSuccessful) {

                        for (doc in it.result) {
                            if (selectedUserName == doc.data.getValue("Username") as String)
                            {
                                userId = doc.data.getValue("UID") as String
                                println("User id $userId")
                            }
                        }

                        drG.get().addOnCompleteListener {
                            if (it.isSuccessful) {

                                for (doc in it.result) {
                                    val users = doc.data.getValue("users") as ArrayList<String>
                                    val d = db.collection("Groups").document(groupId)
                                    if(users.size == 1)
                                    {
                                        d.delete().addOnCompleteListener {
                                            println("Its successfull")

                                        }.addOnFailureListener {
                                            println("Hata >> ${it.localizedMessage}")
                                        }
                                    }
                                    else {
                                        if (userId in users) {
                                            val admin = doc.data.getValue("admin") as String


                                            d.update("users", FieldValue.arrayRemove(userId))
                                                .addOnCompleteListener {
                                                    println("Its successfull")

                                                }.addOnFailureListener {
                                                println("Hata >> ${it.localizedMessage}")
                                            }
                                            if (admin == currentUserId) {

                                                d.update("admin", users.get(0))
                                                    .addOnCompleteListener {
                                                        println("Its successfull")

                                                    }.addOnFailureListener {
                                                    println("Hata >> ${it.localizedMessage}")
                                                }
                                            }

                                        }
                                    }
                                }


                            }
                        }

                    }
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return memberNames.size
    }

}