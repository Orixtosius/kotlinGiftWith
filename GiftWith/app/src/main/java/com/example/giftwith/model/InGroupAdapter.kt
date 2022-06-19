package com.example.giftwith.model

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.giftwith.HomeActivity
import com.example.giftwith.databinding.ActivityEditGroupBinding
import com.example.giftwith.databinding.RecyclerRowBinding
import com.example.giftwith.othersProfileActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class InGroupAdapter(val memberNames : ArrayList<String>):
    RecyclerView.Adapter<InGroupAdapter.memberHolder>() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    class memberHolder(val binding:RecyclerRowBinding): RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): memberHolder {
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return InGroupAdapter.memberHolder(binding)
    }

    override fun onBindViewHolder(holder: memberHolder, position: Int) {
        val currentUser = auth.currentUser
        val userUid = currentUser?.uid!!
        var username = ""
        val intent = Intent(holder.itemView.context, othersProfileActivity::class.java)
        val intentToHome = Intent(holder.itemView.context, HomeActivity::class.java)


        holder.binding.groupnames.text = memberNames.get(position)
        holder.itemView.setOnClickListener {

            val docRef = db.collection("Profiles")
            currentUser?.let {
                docRef.get().addOnCompleteListener {
                    if (it.isSuccessful) {

                        for (doc in it.result) {
                            if (userUid == doc.data.getValue("UID") as String)
                            {
                                username = doc.data.getValue("Username") as String
                            }
                        }
                        if (memberNames.get(position) == username){
                            holder.itemView.context.startActivity(intentToHome)
                        }
                        else{
                            intent.putExtra("memberPos", memberNames.get(position))
                            intent.putExtra("name", memberNames)
                            holder.itemView.context.startActivity(intent)
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
