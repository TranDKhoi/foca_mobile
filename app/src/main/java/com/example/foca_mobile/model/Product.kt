package com.example.foca_mobile.model


data class Product(
    val name: String?,
    val price: Int = 0,
    val image: String?,
    val description: String?,
    val type: String?
)