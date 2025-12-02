package com.example.realtimestockpricetracker.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.realtimestockpricetracker.viewmodel.RealTimeStockViewModel

sealed class Screen(val route: String) {
    object Feed : Screen("feed")
    object Details : Screen("details/{symbol}") {
        fun create(symbol: String) = "details/$symbol"
    }
}

@Composable
fun StockNavHost(viewModel: RealTimeStockViewModel, deepLink: String? = null, onToggleTheme: () -> Unit, isDarkThemeState: MutableState<Boolean>) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Feed.route) {
        composable(Screen.Feed.route) {
            StockListScreen(viewModel = viewModel, onStockClick = { s -> navController.navigate(
                Screen.Details.create(s)
            ) }, onToggleTheme, isDarkThemeState)
            deepLink?.let { navController.navigate(Screen.Details.create(it)) }
        }
        composable(Screen.Details.route, arguments = listOf(navArgument("symbol") { type = NavType.StringType })) { backStackEntry ->
            val symbol = backStackEntry.arguments?.getString("symbol").orEmpty()
            DetailsScreen(symbol = symbol, viewModel = viewModel)
        }
    }
}