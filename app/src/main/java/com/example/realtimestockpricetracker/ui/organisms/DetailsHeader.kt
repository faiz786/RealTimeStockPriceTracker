package com.example.realtimestockpricetracker.ui.organisms

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.realtimestockpricetracker.model.Stock
import com.example.realtimestockpricetracker.ui.molecules.PriceChangeRow

@Composable
fun DetailsHeader(stock: Stock) {
    val change = stock.price - stock.previousPrice

    Column {
        Text(
            stock.symbol,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(12.dp))

        PriceChangeRow(price = stock.price, change = change)

        Spacer(Modifier.height(16.dp))
    }
}
