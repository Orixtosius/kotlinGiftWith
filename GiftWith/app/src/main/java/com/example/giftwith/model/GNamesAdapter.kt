package com.example.giftwith.model

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.giftwith.IngroupActivity
import com.example.giftwith.databinding.RecyclerRowBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class GNamesAdapter(private val groupnames : ArrayList<String>): RecyclerView.Adapter<GNamesAdapter.NameHolder>() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    class NameHolder(val binding: RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NameHolder {
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NameHolder(binding)
    }

    override fun onBindViewHolder(holder: NameHolder, position: Int) {
        holder.binding.groupnames.text = groupnames.get(position)
        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView.context,IngroupActivity::class.java)
            val gname = groupnames.get(position)

            var dr = db.collection("Groups")
            dr.get().addOnCompleteListener {
                if (it.isSuccessful) {
                    for (doc in it.result) {
                        if (gname == doc.data.getValue("groupName") as String) {
                            val groupId = doc.id
                            intent.putExtra("groupName",gname)
                            intent.putExtra("id",groupId)
                            holder.itemView.context.startActivity(intent)
                        }
                    }
                }
            }


        }
    }

    override fun getItemCount(): Int {
        return groupnames.size
    }
}