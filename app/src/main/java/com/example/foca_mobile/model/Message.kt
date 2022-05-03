package com.example.foca_mobile.model

import java.util.*

data class Message(
    var text: String? = null,
    var senderId: Int? = null,
    var id: String? = null,
    var createdAt: Date? = null,
    val updatedAt: Date? = null,
    val roomId: Int? = null,
    val sender: User? = null,
)
