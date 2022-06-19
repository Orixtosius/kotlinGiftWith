package com.example.giftwith.model

import com.google.firebase.firestore.PropertyName

data class items(@get: PropertyName("userID") @set: PropertyName("userID") var userID: String = "",
                 @get: PropertyName("listType") @set: PropertyName("listType") var listType: String = "",
                 @get: PropertyName("items") @set: PropertyName("items") var items:
                 ArrayList<String> = ArrayList<String>())

data class itemInfo(@get: PropertyName("itemID") @set: PropertyName("itemID") var itemID: String = "",
            @get: PropertyName("itemName") @set: PropertyName("itemName") var itemName: String = "",
            @get: PropertyName("itemUrl") @set: PropertyName("itemUrl") var itemUrl: String = "")


