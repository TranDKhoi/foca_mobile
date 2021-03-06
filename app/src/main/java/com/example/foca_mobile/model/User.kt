package com.example.foca_mobile.model


data class User(
    val username: String,
    val password: String? = null,
    val id: Int? = null,
    val firstName : String? = null,
    val lastName : String? = null,
    var phoneNumber: String? = null,
    val role: String? = null,
    val photoUrl: String? = null,
    val fullName: String? = null,
    val accessToken: String? = null,
    val email: String? = null,
)