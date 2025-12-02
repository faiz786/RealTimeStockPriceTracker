package com.example.realtimestockpricetracker.model

import kotlinx.serialization.Serializable

@Serializable
data class StockUpdateDto(
    val symbol: String,
    val price: Double
)
