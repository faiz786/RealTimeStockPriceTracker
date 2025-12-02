package com.example.realtimestockpricetracker.ui.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun StatusDot(connected: Boolean) {
    Box(
        modifier = Modifier
            .size(12.dp)
            .background(
                color = if (connected) Color.Green else Color.Red,
                shape = CircleShape
            )
    )
}
