package com.example.foca_mobile.models

data class Post(
    val name: String?,
    val price: Int = 0,
    val image: String?,
    val description: String?,
    val type: String?
)