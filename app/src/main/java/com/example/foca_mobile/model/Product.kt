package com.example.foca_mobile.model

import android.widget.Button
import java.io.Serializable


data class Product(
    var id: Int? = null,
    var name: String?,
    var price: Int = 0,
    var image: String?,
    var description: String?,
    var type: String?,
    var orderCount : Int = 0,
    var averageRating : Float? = null,
    var reviewCount : Int? = null,
    var createdAt : String? = null,
    var updatedAt : String? = null,
    var deletedAt : String? = null
) : Serializable