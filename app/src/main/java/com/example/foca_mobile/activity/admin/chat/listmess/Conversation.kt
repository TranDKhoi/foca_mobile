package com.example.foca_mobile.activity.admin.chat.listmess

import android.os.Parcel
import android.os.Parcelable
import com.example.foca_mobile.model.Message
import com.example.foca_mobile.model.User
import java.util.*

data class Conversation(
    val id: Int? = null,
    val createdAt: Date? = null,
    val lastMessage: Message? = null,
    val members: List<User>? = null,
    val updatedAt: Date? = null
)