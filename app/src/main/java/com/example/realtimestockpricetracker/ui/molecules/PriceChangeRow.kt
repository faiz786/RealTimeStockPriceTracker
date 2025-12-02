package com.example.realtimestockpricetracker.ui.molecules

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.realtimestockpricetracker.ui.atoms.ArrowIcon
import com.example.realtimestockpricetracker.ui.atoms.PriceText
import java.text.DecimalFormat

@Composable
fun PriceChangeRow(price: Double?, change: Double) {
    val df = DecimalFormat("#,###.##")
    val isUp = change >= 0
    val color = if (isUp) Color.Green else Color.Red

    Row(verticalAlignment = Alignment.CenterVertically) {

        price?.let { PriceText(df.format(it), color) }

        Spacer(Modifier.width(8.dp))

        ArrowIcon(isUp = isUp, color = color)

        Spacer(Modifier.width(4.dp))

        Text(
            text = df.format(kotlin.math.abs(change)),
            color = color,
        )
    }
}
