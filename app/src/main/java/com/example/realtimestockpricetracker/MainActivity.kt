package com.example.realtimestockpricetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.mutableStateOf
import com.example.realtimestockpricetracker.ui.screens.StockNavHost
import com.example.realtimestockpricetracker.ui.screens.StockTheme
import com.example.realtimestockpricetracker.viewmodel.RealTimeStockViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val vm: RealTimeStockViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val deep = intent?.data?.lastPathSegment
        val isDarkTheme = mutableStateOf(false)
        setContent {
            StockTheme(darkTheme = isDarkTheme.value) {
                StockNavHost(
                    viewModel = vm,
                    deepLink = deep,
                    onToggleTheme = { isDarkTheme.value = !isDarkTheme.value },
                    isDarkTheme
                )
            }
        }
    }
}