package com.example.foca_mobile.model

import java.io.Serializable

data class Review(
    var id: Int? = null,
    var userId: Int? = null,
    var orderDetail: OrderDetails? = null,
    var orderDetailId : Int? = orderDetail?.id,
    var rating: Int? = null,
    var content: String? = null
) : Serializable
