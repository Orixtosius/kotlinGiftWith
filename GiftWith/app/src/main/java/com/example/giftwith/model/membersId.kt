package com.example.giftwith.model

import com.google.firebase.firestore.PropertyName

data class membersId(@get: PropertyName("groupName") @set: PropertyName("groupName") var groupName: String = "",
                     @get: PropertyName("users") @set: PropertyName("users") var users: ArrayList<String> = ArrayList())

data class memberIdStr(var ids: ArrayList<membersId> = ArrayList())