package com.example.realtimestockpricetracker

import app.cash.turbine.test
import com.example.realtimestockpricetracker.viewmodel.RealTimeStockViewModel
import com.example.realtimestockpricetracker.ws.WebSocketManager
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RealTimeStockViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val wsFlow = MutableSharedFlow<String>()

    private lateinit var viewModel: RealTimeStockViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        val mockWs = mockk<WebSocketManager>(relaxed = true)

        every { mockWs.incomingMessages() } returns wsFlow

        viewModel = RealTimeStockViewModel(mockWs)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `emit messages updates state`() = runTest {
        viewModel.toggleFeed()
        advanceUntilIdle()

        wsFlow.emit("AAPL|150.0")
        wsFlow.emit("GOOG|2500.0")

        viewModel.state.test {
            skipItems(1) // initial state

            val s1 = awaitItem()
            assert(s1.stocks.any { it.symbol == "AAPL" && it.price == 150.0 })

            val s2 = awaitItem()
            assert(s2.stocks.any { it.symbol == "GOOG" && it.price == 2500.0 })
        }
    }
}




