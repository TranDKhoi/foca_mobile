package com.example.foca_mobile.activity.user.cart_order.`object`

data class Order(
    var name: String,
    var totalPrice: Int,
    var status: String,
    var listFood: ArrayList<Food>
    )