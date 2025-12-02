package com.example.realtimestockpricetracker.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.realtimestockpricetracker.reducer.StockReducer
import com.example.realtimestockpricetracker.data.StockRepository
import com.example.realtimestockpricetracker.intents.StockIntent
import com.example.realtimestockpricetracker.model.Stock
import com.example.realtimestockpricetracker.uievents.StockAction
import com.example.realtimestockpricetracker.uievents.StockUIState
import com.example.realtimestockpricetracker.ws.WebSocketManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class RealTimeStockViewModel @Inject constructor(
    private val wsManager: WebSocketManager
) : ViewModel() {

    private val _state = MutableStateFlow(
        StockUIState(
            isConnected = false,
            isRunning = false,
            stocks = StockRepository.symbols.map { s ->
                Stock(symbol = s, description = "Description for $s", price = (100..500).random() + Math.random())
            }.sortedByDescending { it.price }
        )
    )
    val state: StateFlow<StockUIState> = _state.asStateFlow()

    private var sendJob: Job? = null
    private var incomingJob: Job? = null

    fun processIntent(intent: StockIntent) {
        when (intent) {
            is StockIntent.ToggleFeed -> {
                if (state.value.isRunning) processIntent(StockIntent.StopFeed) else processIntent(StockIntent.StartFeed)
            }
            is StockIntent.StartFeed -> startFeed()
            is StockIntent.StopFeed -> stopFeed()
        }
    }

    private fun startFeed() {
        if (state.value.isRunning) return

        incomingJob = viewModelScope.launch {
            wsManager.incomingMessages()
                .onEach { raw ->
                    when {
                        raw == "::CONNECTED::" -> dispatch(StockAction.SetConnected(true))
                        raw.startsWith("::ERROR::") -> {
                            dispatch(StockAction.SetConnected(false))
                        }
                        raw == "::CLOSED::" || raw == "::DISCONNECTING::" -> dispatch(StockAction.SetConnected(false))
                        else -> {
                            val parts = raw.split("|")
                            if (parts.size == 2) {
                                val sym = parts[0]
                                val price = parts[1].toDoubleOrNull()
                                if (price != null) {
                                    dispatch(StockAction.ReceivedPrice(sym, price))
                                    viewModelScope.launch {
                                        delay(1000)
                                        dispatch(StockAction.ClearFlash(sym))
                                    }
                                }
                            }
                        }
                    }
                }
                .catch { e -> dispatch(StockAction.SetConnected(false)) }
                .collect()
        }

        sendJob = viewModelScope.launch(Dispatchers.IO) {
            dispatch(StockAction.FeedStarted)
            while (isActive) {
                val snapshot = _state.value.stocks
                snapshot.forEach { stock ->
                    val newPrice = (stock.price * (0.98 + Math.random() * 0.04)).coerceAtLeast(0.01)
                    wsManager.send("${stock.symbol}|${"%.2f".format(newPrice)}")
                }
                delay(2000)
            }
        }
    }

    private fun stopFeed() {
        sendJob?.cancel()
        incomingJob?.cancel()
        wsManager.stop()
        dispatch(StockAction.FeedStopped)
        dispatch(StockAction.SetConnected(false))
    }

    private fun dispatch(action: StockAction) {
        val updated = StockReducer.reduce(_state.value, action)
        _state.value = updated
    }

    override fun onCleared() {
        stopFeed()
        super.onCleared()
    }
}