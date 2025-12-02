package com.example.realtimestockpricetracker.ui.atoms

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.realtimestockpricetracker.R

@Composable
fun ArrowIcon(isUp: Boolean, color: Color) {
    Image(
        painter = painterResource(
            id = if (isUp) R.drawable.ic_up_arrow else R.drawable.ic_down_arrow
        ),
        contentDescription = null,
        colorFilter = ColorFilter.tint(color),
        modifier = Modifier.size(10.dp)
    )
}
