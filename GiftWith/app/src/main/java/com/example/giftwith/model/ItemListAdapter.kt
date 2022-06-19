package com.example.giftwith.model

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.giftwith.databinding.ActivityItemListBinding
import com.example.giftwith.databinding.RecyclerItemRowBinding
import com.example.giftwith.databinding.RecyclerRowBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class ItemListAdapter(val items: ArrayList<String>, val control:Boolean, val type: String):
    RecyclerView.Adapter<ItemListAdapter.itemHolder>() {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    class itemHolder(val binding: RecyclerItemRowBinding,items: ArrayList<String>,type: String): RecyclerView.ViewHolder(binding.root){

        val deleteButton = binding.deleteIcon
        val editButton = binding.editIcon

        val likeButton = binding.thumbUpIcon
        val dislikeButton = binding.thumbDownIcon

        fun deleteItem(index: String,db: FirebaseFirestore){
            val docRef = db.collection("List2")
            deleteButton.setOnClickListener {
                docRef.addSnapshotListener { value, error ->
                    if(error == null){
                        if(value != null){
                            for(doc in value){
                                val items = doc.data.getValue("items") as ArrayList<String>
                                if(index in items){
                                    println("DOC ID : ${doc.id}")
                                    docRef.document(doc.id).update("items", FieldValue.arrayRemove(index)).addOnSuccessListener {
                                        println("SUCCESS")
                                    }
                                }

                            }
                        }
                    }
                }
            }
        }
        fun editItem(index: String, db: FirebaseFirestore){
            val docRef = db.collection("List2")

            editButton.setOnClickListener {
                    docRef.addSnapshotListener { value, error ->
                        if (error == null) {
                            if (value != null) {
                                for (doc in value) {
                                    val items = doc.data.getValue("items") as ArrayList<String>
                                    if (index in items) {

                                    }

                                }
                            }
                        }
                    }


            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): itemHolder {
        val binding = RecyclerItemRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ItemListAdapter.itemHolder(binding,items,type)
    }

    override fun onBindViewHolder(holder: itemHolder, position: Int) {
        val itemName = items.get(position)
        holder.binding.itemName.text = items.get(position)
        db.collection("List2").addSnapshotListener { value, error ->
            if(error == null){
                if(value != null){
                    for(doc in value){
                        var lT = doc.data.getValue("listType") as String
                        var itemArray = doc.data.getValue("items") as ArrayList<String>
                        if(itemName in itemArray && lT == type){
                            holder.deleteItem(items.get(position),db)
//                            holder.editItem(items.get(position),db)
                        }
                    }
                }
            }
        }

        if(type == "Wish") {
            if (control == true) {
                holder.binding.thumbDownIcon.visibility = View.INVISIBLE
                holder.binding.thumbUpIcon.visibility = View.INVISIBLE
                holder.binding.deleteIcon.visibility = View.VISIBLE
                holder.binding.editIcon.visibility = View.VISIBLE
            } else {
                holder.binding.thumbDownIcon.visibility = View.VISIBLE
                holder.binding.thumbUpIcon.visibility = View.VISIBLE
                holder.binding.deleteIcon.visibility = View.INVISIBLE
                holder.binding.editIcon.visibility = View.INVISIBLE
            }
        }
        else {
            if (control == true) {
                holder.binding.thumbDownIcon.visibility = View.VISIBLE
                holder.binding.thumbUpIcon.visibility = View.VISIBLE
                holder.binding.deleteIcon.visibility = View.INVISIBLE
                holder.binding.editIcon.visibility = View.INVISIBLE
            } else {
                holder.binding.thumbDownIcon.visibility = View.INVISIBLE
                holder.binding.thumbUpIcon.visibility = View.INVISIBLE
                holder.binding.deleteIcon.visibility = View.VISIBLE
                holder.binding.editIcon.visibility = View.VISIBLE
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

}