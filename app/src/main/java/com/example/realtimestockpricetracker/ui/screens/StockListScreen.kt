package com.example.realtimestockpricetracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.realtimestockpricetracker.intents.StockIntent
import com.example.realtimestockpricetracker.ui.organisms.StockListAppBar
import com.example.realtimestockpricetracker.ui.organisms.StockRowItem
import com.example.realtimestockpricetracker.viewmodel.RealTimeStockViewModel
import kotlinx.coroutines.launch

@Composable
fun StockListScreen(
    viewModel: RealTimeStockViewModel,
    onStockClick: (String) -> Unit,
    onToggleTheme: () -> Unit,
    isDarkThemeState: MutableState<Boolean>
) {
    val state by viewModel.state.collectAsState()

    val scope = rememberCoroutineScope()

    val isDarkTheme by isDarkThemeState

    Scaffold(
        topBar = {
            StockListAppBar(
                isConnected = state.isConnected,
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme,
                isRunning = state.isRunning,
                onToggleFeed = {
                    scope.launch { viewModel.processIntent(StockIntent.ToggleFeed) }
                }
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            items(state.stocks) { stock ->

                StockRowItem(
                    stock = stock,
                    onClick = onStockClick
                )
            }

            item { Spacer(Modifier.height(40.dp)) }
        }
    }
}
