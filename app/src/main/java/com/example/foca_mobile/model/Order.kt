package com.example.foca_mobile.model

import java.io.Serializable

data class Order(
    var id: Int? = null,
    var surcharge: Int? = 0,
    var totalPrice: Int? = 0,
    var notes: String? = null,
    var isReviewed: Boolean = false,
    var status: String? = null,
    var createdAt: String? = null,
    var updatedAt: String? = null,
    var buyerId: Int? = null,
    var orderDetails: MutableList<OrderDetails>? = null,
    var buyer: User? = null
) : Serializable