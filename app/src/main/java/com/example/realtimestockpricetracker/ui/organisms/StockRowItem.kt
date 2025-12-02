package com.example.realtimestockpricetracker.ui.organisms

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.realtimestockpricetracker.model.Stock
import com.example.realtimestockpricetracker.ui.molecules.PriceChangeRow
import java.text.DecimalFormat

@Composable
fun StockRowItem(
    stock: Stock,
    onClick: (String) -> Unit
) {
    val df = DecimalFormat("#,###.##")
    val change = stock.price - stock.previousPrice

    val color = if (change >= 0) Color.Green else Color.Red
    val defaultTextColor = MaterialTheme.colorScheme.onSurface

    val flashColor = remember { Animatable(defaultTextColor) }

    LaunchedEffect(defaultTextColor) {
        flashColor.snapTo(defaultTextColor)
    }

    LaunchedEffect(change) {
        if (change != 0.0) {
            flashColor.snapTo(color)
            flashColor.animateTo(
                targetValue = defaultTextColor,
                animationSpec = tween(durationMillis = 1000)
            )
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(stock.symbol) }
            .padding(vertical = 12.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column {
            Text(
                text = stock.symbol,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = stock.description,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Column(horizontalAlignment = Alignment.End) {

            Text(
                df.format(stock.price),
                color = flashColor.value,
                fontWeight = FontWeight.Medium,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(Modifier.height(4.dp))

            PriceChangeRow(price = null, change = change)
        }
    }
}
