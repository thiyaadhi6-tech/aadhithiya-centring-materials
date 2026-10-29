package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.model.AppScreen
import com.example.ui.screens.CounterSalesScreen
import com.example.ui.screens.CustomerBalanceScreen
import com.example.ui.screens.DailyReportScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.MonthlyReportScreen
import com.example.ui.screens.ProfitLossScreen
import com.example.ui.screens.PurchaseEntryScreen
import com.example.ui.screens.SalesBillScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.StockManagementScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.FabricationViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: FabricationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppNavHost(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppNavHost(viewModel: FabricationViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val language by viewModel.language.collectAsState()
    val feedbackMessage by viewModel.feedbackMessage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(feedbackMessage) {
        feedbackMessage?.let { msg ->
            snackbarHostState.showSnackbar(message = msg, duration = SnackbarDuration.Short)
            viewModel.clearFeedback()
        }
    }

    // Intercept hardware and gesture back
    BackHandler(enabled = currentScreen != AppScreen.SPLASH && currentScreen != AppScreen.DASHBOARD) {
        viewModel.handleBack()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        AnimatedContent(
            targetState = currentScreen,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            label = "ScreenTransition"
        ) { screen ->
            when (screen) {
                AppScreen.SPLASH -> {
                    SplashScreen(
                        onLoginClick = { viewModel.navigateTo(AppScreen.DASHBOARD) },
                        onNewUserClick = { viewModel.navigateTo(AppScreen.DASHBOARD) },
                        language = language
                    )
                }

                AppScreen.DASHBOARD -> {
                    DashboardScreen(viewModel = viewModel)
                }

                AppScreen.COUNTER_SALES -> {
                    CounterSalesScreen(viewModel = viewModel)
                }

                AppScreen.STOCK_MANAGEMENT -> {
                    StockManagementScreen(viewModel = viewModel)
                }

                AppScreen.PURCHASE_ENTRY -> {
                    PurchaseEntryScreen(viewModel = viewModel)
                }

                AppScreen.DAILY_REPORT -> {
                    DailyReportScreen(viewModel = viewModel)
                }

                AppScreen.MONTHLY_REPORT -> {
                    MonthlyReportScreen(viewModel = viewModel)
                }

                AppScreen.CUSTOMER_BALANCE -> {
                    CustomerBalanceScreen(viewModel = viewModel)
                }

                AppScreen.PROFIT_LOSS -> {
                    ProfitLossScreen(viewModel = viewModel)
                }

                AppScreen.SALES_BILL_VIEW -> {
                    SalesBillScreen(viewModel = viewModel)
                }
            }
        }
    }
}
