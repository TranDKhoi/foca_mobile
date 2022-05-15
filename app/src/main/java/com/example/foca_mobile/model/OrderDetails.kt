package com.example.foca_mobile.model

import java.io.Serializable


data class OrderDetails(
    var id: Int? = null,
    var orderId: Int? = null,
    var productId: Int? = null,
    var price: Int? = 0,
    var quantity: Int? = 0,
    var product: Product? = null
) : Serializable