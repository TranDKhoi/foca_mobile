package com.example.foca_mobile.model

data class Data(
    val buyerId: Int,
    val createdAt: String,
    val id: Int,
    val isReviewed: Boolean,
    val notes: String,
    val orderDetails: List<OrderDetails>,
    val status: String,
    val totalPrice: Int,
    val updatedAt: String
)