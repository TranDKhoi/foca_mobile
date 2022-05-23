package com.example.foca_mobile.model

data class Review(
    var id: Int? = null,
    var userId: Int? = null,
    var orderDetail: OrderDetails? = null,
    var orderDetailId: Int? = null,
    var rating: Int? = 5,
    var content: String? = null,
    var createdAt: String? = null,
    var updatedAt: String? = null,
    var deletedAt: String? = null,
    var reviews: Review? = null,
    var user: User? = null
)