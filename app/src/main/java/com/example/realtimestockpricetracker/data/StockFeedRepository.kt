package com.example.realtimestockpricetracker.data

import com.example.realtimestockpricetracker.model.Stock
import com.example.realtimestockpricetracker.model.StockUpdateDto
import com.example.realtimestockpricetracker.ws.WebSocketManager
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class StockFeedRepository @Inject constructor(
    private val ws: WebSocketManager
) {

    fun sendPriceUpdate(stock: Stock, newPrice: Double) {
        val dto = StockUpdateDto(
            symbol = stock.symbol,
            price = newPrice
        )
        val json = Json.encodeToString(dto)
        ws.send(json)
    }
}
