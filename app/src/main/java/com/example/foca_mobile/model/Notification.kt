package com.example.foca_mobile.model

import java.util.*

data class Notification(
    var id: String? = null,
    var message: String? = null,
    var iconType: String? = null,
    var isSeen: Boolean = false,
    var createdAt: Date? = null,
    var updatedAt: Date? = null,
    var userId: String? = null
)
