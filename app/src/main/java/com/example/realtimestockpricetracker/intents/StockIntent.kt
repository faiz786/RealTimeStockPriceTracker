package com.example.realtimestockpricetracker.intents

sealed class StockIntent {
    data object ToggleFeed : StockIntent()
    data object StartFeed : StockIntent()
    data object StopFeed : StockIntent()
}