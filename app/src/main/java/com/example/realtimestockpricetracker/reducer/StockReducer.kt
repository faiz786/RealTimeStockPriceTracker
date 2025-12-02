package com.example.realtimestockpricetracker.reducer

import com.example.realtimestockpricetracker.model.FlashColor
import com.example.realtimestockpricetracker.uievents.StockAction
import com.example.realtimestockpricetracker.uievents.StockUIState


object StockReducer {
    fun reduce(state: StockUIState, action: StockAction): StockUIState {
        return when (action) {
            is StockAction.SetConnected -> state.copy(isConnected = action.connected)
            is StockAction.FeedStarted -> state.copy(isRunning = true)
            is StockAction.FeedStopped -> state.copy(isRunning = false)
            is StockAction.ReceivedPrice -> {
                val updated = state.stocks.map { s ->
                    if (s.symbol == action.symbol) {
                        val previous = s.price
                        val flash = when {
                            action.price > previous -> FlashColor.GREEN
                            action.price < previous -> FlashColor.RED
                            else -> null
                        }
                        s.copy(previousPrice = previous, price = action.price, flashColor = flash)
                    } else s
                }.sortedByDescending { it.price }
                state.copy(stocks = updated)
            }
            is StockAction.ClearFlash -> {
                val updated = state.stocks.map { s ->
                    if (s.symbol == action.symbol) s.copy(flashColor = null) else s
                }
                state.copy(stocks = updated)
            }
        }
    }
}