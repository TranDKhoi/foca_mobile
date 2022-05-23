package com.example.foca_mobile.model

data class Cart(
    var id : Int,
    var quantity : Int,
    var userId : Int,
    var productId : Int,
    var product : Product? = null
)
