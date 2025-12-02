package com.example.realtimestockpricetracker.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.realtimestockpricetracker.ui.atoms.StockAppBar
import com.example.realtimestockpricetracker.ui.organisms.DetailsHeader
import com.example.realtimestockpricetracker.viewmodel.RealTimeStockViewModel

@Composable
fun DetailsScreen(symbol: String, viewModel: RealTimeStockViewModel) {
    val state by viewModel.state.collectAsState()
    val stock = state.stocks.firstOrNull { it.symbol == symbol }

    Scaffold(
        topBar = { StockAppBar(title = { Text(symbol) }) }
    ) { padding ->

        if (stock == null) {
            Text("Stock not found", Modifier.padding(16.dp))
        } else {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
            ) {

                DetailsHeader(stock)

                Text(stock.description)
            }
        }
    }
}
