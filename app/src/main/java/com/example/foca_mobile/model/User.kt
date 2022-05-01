package com.example.foca_mobile.model

import java.io.Serializable


data class User(
    val username: String,
    val password: String,
) : Serializable {
    val id: String? = null;
    val name: String? = null;
    var phoneNumber: String? = null;
    val role: String? = null;
    val photoUrl: String? = null;
    val fullName: String? = null;
    val accessToken: String? = null;
    val email: String? = null;
}