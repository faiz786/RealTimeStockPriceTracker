package com.example.realtimestockpricetracker.uievents

sealed class StockAction {
    data class ReceivedPrice(val symbol: String, val price: Double) : StockAction()
    data class SetConnected(val connected: Boolean) : StockAction()
    data object FeedStarted : StockAction()
    data object FeedStopped : StockAction()
    data class ClearFlash(val symbol: String) : StockAction()
}