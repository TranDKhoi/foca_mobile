package com.example.foca_mobile.model

import java.io.Serializable


data class Product(
    var id: Int? = null,
    var name: String?,
    var price: Int = 0,
    var image: String?,
    var description: String?,
    var type: String?,
//    var createdAt : String?,
//    var updatedAt : String?,
//    var deletedAt : String?
) : Serializable