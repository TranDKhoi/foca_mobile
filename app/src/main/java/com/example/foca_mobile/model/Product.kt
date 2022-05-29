package com.example.foca_mobile.model


data class Product(
    var createdAt: String?,
    var deletedAt: Any?,
    var description: String?,
    var id: Int?,
    var image: String?,
    var name: String?,
    var price: Int?,
    var type: String?,
    var updatedAt: String?,
    var reviews: MutableList<Review>?,
    var orderCount: Int = 0,
    var averageRating: Float? = null,
    var reviewCount: Int? = null,
    var isFavorited: Boolean? = null
)