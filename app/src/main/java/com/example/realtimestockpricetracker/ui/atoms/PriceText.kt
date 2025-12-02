package com.example.realtimestockpricetracker.ui.atoms

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@Composable
fun PriceText(value: String, color: Color, weight: FontWeight = FontWeight.Medium) {
    Text(
        text = value,
        color = color,
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = weight
    )
}
