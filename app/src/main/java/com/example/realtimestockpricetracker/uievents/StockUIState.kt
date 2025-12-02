package com.example.realtimestockpricetracker.uievents

import com.example.realtimestockpricetracker.model.Stock


data class StockUIState(
    val isConnected: Boolean = false,
    val isRunning: Boolean = false,
    val stocks: List<Stock> = emptyList()
)