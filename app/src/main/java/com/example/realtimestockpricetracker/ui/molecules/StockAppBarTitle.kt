package com.example.realtimestockpricetracker.ui.molecules

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.example.realtimestockpricetracker.ui.atoms.StatusDot

@Composable
fun StockAppBarTitle(isConnected: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        StatusDot(isConnected)
        Spacer(Modifier.width(8.dp))
        Text(
            "Stock Price Tracker",
            style = MaterialTheme.typography.titleLarge
        )
    }
}
