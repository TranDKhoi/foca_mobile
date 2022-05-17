package com.example.foca_mobile.model

data class Filter(
    var type: String? = null,
    var sort: String? = null,
    var range: MutableList<Float> = mutableListOf(),
    var way: Boolean? = null
)