package com.example.realtimestockpricetracker.ui.organisms

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.realtimestockpricetracker.R
import com.example.realtimestockpricetracker.ui.atoms.StockAppBar
import com.example.realtimestockpricetracker.ui.molecules.StockAppBarTitle

@Composable
fun StockListAppBar(
    isConnected: Boolean,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    onToggleFeed: () -> Unit,
    isRunning: Boolean
) {
    StockAppBar(
        title = { StockAppBarTitle(isConnected) },
        actions = {
            IconToggleButton (checked = isDarkTheme, onCheckedChange = { onToggleTheme() }) {
                Image(
                    painter = painterResource(
                        id = if (isDarkTheme) R.drawable.ic_sun 
                             else R.drawable.ic_half_moon
                    ),
                    contentDescription = null,
                    colorFilter = null,
                    modifier = Modifier.size(16.dp)
                )
            }

            TextButton(onClick = onToggleFeed) {
                Text(if (isRunning) "Stop" else "Start")
            }
        }
    )
}
