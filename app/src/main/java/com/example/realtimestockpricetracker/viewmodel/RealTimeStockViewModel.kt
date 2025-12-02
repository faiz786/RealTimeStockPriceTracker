package com.example.realtimestockpricetracker.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.realtimestockpricetracker.data.StockRepository
import com.example.realtimestockpricetracker.model.FlashColor
import com.example.realtimestockpricetracker.model.Stock
import com.example.realtimestockpricetracker.uievents.StockUIState
import com.example.realtimestockpricetracker.ws.WebSocketManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class RealTimeStockViewModel @Inject constructor(
    private val wsManager: WebSocketManager
) : ViewModel() {

    private val _state = MutableStateFlow(
        StockUIState(
            isConnected = false,
            isRunning = false,
            stocks = StockRepository.symbols.map { s ->
                Stock(symbol = s, description = "Description for $s", price = (100..500).random() + Random.nextDouble())
            }.sortedByDescending { it.price }
        )
    )
    val state: StateFlow<StockUIState> = _state.asStateFlow()

    private var senderJob: Job? = null
    private var receiverJob: Job? = null

    /** Toggle Start/Stop feed **/
    fun toggleFeed() {
        if (state.value.isRunning) stopFeed() else startFeed()
    }

    fun startFeed() {
        if (state.value.isRunning) return
        _state.update { it.copy(isRunning = true) }

        receiverJob = viewModelScope.launch {
            wsManager.incomingMessages()
                .onEach { raw ->
                    when {
                        raw == "::CONNECTED::" -> _state.update { it.copy(isConnected = true) }
                        raw.startsWith("::ERROR::") || raw == "::CLOSED::" || raw == "::DISCONNECTING::" ->
                            _state.update { it.copy(isConnected = false) }
                        else -> {
                            // format: SYMBOL|PRICE (we send like that)
                            val parts = raw.split("|")
                            if (parts.size == 2) {
                                val sym = parts[0]
                                val price = parts[1].toDoubleOrNull()
                                if (price != null) applyPrice(sym, price)
                            }
                        }
                    }
                }
                .catch { e ->
                    _state.update { it.copy(isConnected = false) }
                }
                .collect()
        }

        senderJob = viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                val snapshot = _state.value.stocks
                snapshot.forEach { stock ->
                    val newPrice = (stock.price * (0.98 + Random.nextDouble() * 0.04)).coerceAtLeast(0.01)
                    val message = "${stock.symbol}|${"%.2f".format(newPrice)}"
                    wsManager.send(message)
                }
                delay(2000)
            }
        }
    }

    private fun applyPrice(symbol: String, price: Double) {
        val updated = _state.value.stocks.map { s ->
            if (s.symbol == symbol) {
                val flash = when {
                    price > s.price -> FlashColor.GREEN
                    price < s.price -> FlashColor.RED
                    else -> null
                }
                s.copy(previousPrice = s.price, price = price, flashColor = flash)
            } else s
        }.sortedByDescending { it.price }

        _state.update { it.copy(stocks = updated) }

        // clear flash after 1s
        viewModelScope.launch {
            delay(1000)
            _state.update {
                it.copy(stocks = it.stocks.map { s -> if (s.symbol == symbol) s.copy(flashColor = null) else s })
            }
        }
    }

    fun stopFeed() {
        senderJob?.cancel()
        receiverJob?.cancel()
        wsManager.stop()
        _state.update { it.copy(isRunning = false, isConnected = false) }
    }

    override fun onCleared() {
        stopFeed()
        super.onCleared()
    }
}