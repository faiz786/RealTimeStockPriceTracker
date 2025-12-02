package com.example.realtimestockpricetracker.model

data class Stock(
    val symbol: String,
    val description: String = "",
    val price: Double,
    val previousPrice: Double = price,
    val flashColor: FlashColor? = null
)

enum class FlashColor { GREEN, RED }
