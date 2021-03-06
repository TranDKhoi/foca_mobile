package com.example.foca_mobile.model

import java.util.*

data class Notification(
    var id: Int? = null,
    var message: String? = null,
    var isSeen: Boolean? = false,
    var createdAt: Date? = null,
    var updatedAt: Date? = null,
    var userId: Int? = null,
    var type: String? = null,
    var order: Order? = null
)
