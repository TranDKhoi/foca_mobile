package com.example.foca_mobile.model

data class Room(
    val id: Int? = null,
    val lastMessage: Message? = null,
    val members: List<User>? = null,
    val messages: List<Message>? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)