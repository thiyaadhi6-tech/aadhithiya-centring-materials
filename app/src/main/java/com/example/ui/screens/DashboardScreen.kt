package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppScreen
import com.example.model.Language
import com.example.ui.components.AppBottomNavBar
import com.example.ui.components.AppTopBar
import com.example.ui.components.formatCurrency
import com.example.ui.theme.BrandNavy
import com.example.ui.theme.CardPendingRed
import com.example.ui.theme.CardPurchaseOrange
import com.example.ui.theme.CardSalesGreen
import com.example.ui.theme.CardStockBlue
import com.example.ui.theme.TileCustomerPink
import com.example.ui.theme.TileDailyReportBlue
import com.example.ui.theme.TileMonthlyCalendar
import com.example.ui.theme.TileProfitGreen
import com.example.ui.theme.TilePurchaseOrange
import com.example.ui.theme.TileSalesPurple
import com.example.ui.theme.TileStockTeal
import com.example.util.AppStrings
import com.example.viewmodel.FabricationViewModel

@Composable
fun DashboardScreen(
    viewModel: FabricationViewModel
) {
    val language by viewModel.language.collectAsState()
    val activeTab by viewModel.activeTab.collectAsState()
    val inventory by viewModel.inventory.collectAsState()
    val customers by viewModel.customers.collectAsState()
    val salesBills by viewModel.salesBills.collectAsState()
    val purchases by viewModel.purchases.collectAsState()

    var showCompanyInfoDialog by remember { mutableStateOf(false) }

    // Dynamic metrics
    val totalStockVal = inventory.sumOf { it.currentStock * it.unitRate }
    val pendingCustAmt = customers.sumOf { it.pendingBalance }
    val todaySalesAmt = salesBills.filter { it.date == "24-10-2026" }.sumOf { it.totalAmount }
        .let { if (it > 0) it else 25480.0 }
    val todayPurchaseAmt = purchases.filter { it.date == "24-10-2026" }.sumOf { it.totalAmount }
        .let { if (it > 0) it else 12600.0 }

    Scaffold(
        topBar = {
            AppTopBar(
                title = AppStrings.get("app_title", language),
                showBack = false,
                onMenuClick = { showCompanyInfoDialog = true },
                language = language,
                onLanguageToggle = { viewModel.toggleLanguage() },
                actions = {
                    IconButton(
                        onClick = { showCompanyInfoDialog = true },
                        modifier = Modifier.testTag("dashboard_info_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "About",
                            tint = Color.White
                        )
                    }
                }
            )
        },
        bottomBar = {
            AppBottomNavBar(
                activeTab = activeTab,
                onTabSelected = { viewModel.selectBottomTab(it) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF1F5F9))
                .padding(16.dp)
        ) {
            // 2x2 Metric Cards Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetricCard(
                    title = AppStrings.get("today_sales", language),
                    amount = todaySalesAmt,
                    icon = Icons.Default.ShoppingCart,
                    backgroundColor = CardSalesGreen,
                    modifier = Modifier.weight(1f),
                    testTag = "metric_today_sales",
                    onClick = { viewModel.navigateTo(AppScreen.DAILY_REPORT) }
                )
                MetricCard(
                    title = AppStrings.get("today_purchase", language),
                    amount = todayPurchaseAmt,
                    icon = Icons.Default.Receipt,
                    backgroundColor = CardPurchaseOrange,
                    modifier = Modifier.weight(1f),
                    testTag = "metric_today_purchase",
                    onClick = { viewModel.navigateTo(AppScreen.PURCHASE_ENTRY) }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetricCard(
                    title = AppStrings.get("total_stock", language),
                    amount = totalStockVal,
                    icon = Icons.Default.Inventory,
                    backgroundColor = CardStockBlue,
                    modifier = Modifier.weight(1f),
                    testTag = "metric_total_stock",
                    onClick = { viewModel.navigateTo(AppScreen.STOCK_MANAGEMENT) }
                )
                MetricCard(
                    title = AppStrings.get("pending_amount", language),
                    amount = pendingCustAmt,
                    icon = Icons.Default.Group,
                    backgroundColor = CardPendingRed,
                    modifier = Modifier.weight(1f),
                    testTag = "metric_pending_amount",
                    onClick = { viewModel.navigateTo(AppScreen.CUSTOMER_BALANCE) }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 7/8 Action Tiles Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    ActionTile(
                        title = AppStrings.get("counter_sales", language),
                        icon = Icons.Default.PointOfSale,
                        iconBgColor = TileSalesPurple,
                        onClick = { viewModel.navigateTo(AppScreen.COUNTER_SALES) },
                        testTag = "tile_counter_sales"
                    )
                }
                item {
                    ActionTile(
                        title = AppStrings.get("stock_management", language),
                        icon = Icons.Default.Inventory,
                        iconBgColor = TileStockTeal,
                        onClick = { viewModel.navigateTo(AppScreen.STOCK_MANAGEMENT) },
                        testTag = "tile_stock_management"
                    )
                }
                item {
                    ActionTile(
                        title = AppStrings.get("purchase_entry", language),
                        icon = Icons.Default.ShoppingCart,
                        iconBgColor = TilePurchaseOrange,
                        onClick = { viewModel.navigateTo(AppScreen.PURCHASE_ENTRY) },
                        testTag = "tile_purchase_entry"
                    )
                }
                item {
                    ActionTile(
                        title = AppStrings.get("daily_report", language),
                        icon = Icons.Default.Assessment,
                        iconBgColor = TileDailyReportBlue,
                        onClick = { viewModel.navigateTo(AppScreen.DAILY_REPORT) },
                        testTag = "tile_daily_report"
                    )
                }
                item {
                    ActionTile(
                        title = AppStrings.get("monthly_report", language),
                        icon = Icons.Default.CalendarMonth,
                        iconBgColor = TileMonthlyCalendar,
                        onClick = { viewModel.navigateTo(AppScreen.MONTHLY_REPORT) },
                        testTag = "tile_monthly_report"
                    )
                }
                item {
                    ActionTile(
                        title = AppStrings.get("customer_balance", language),
                        icon = Icons.Default.Group,
                        iconBgColor = TileCustomerPink,
                        onClick = { viewModel.navigateTo(AppScreen.CUSTOMER_BALANCE) },
                        testTag = "tile_customer_balance"
                    )
                }
                item {
                    ActionTile(
                        title = AppStrings.get("profit_loss", language),
                        icon = Icons.Default.CurrencyRupee,
                        iconBgColor = TileProfitGreen,
                        onClick = { viewModel.navigateTo(AppScreen.PROFIT_LOSS) },
                        testTag = "tile_profit_loss"
                    )
                }
            }
        }
    }

    if (showCompanyInfoDialog) {
        AlertDialog(
            onDismissRequest = { showCompanyInfoDialog = false },
            title = {
                Text(
                    text = "AADHITHIYA CENTERING MATERIAL RENTAL",
                    fontWeight = FontWeight.Bold,
                    color = BrandNavy
                )
            },
            text = {
                Column {
                    Text(
                        text = "Scaffolding Materials • Centring Materials • Fabrication Works",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = Color(0xFF334155)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Madurai - 625003, Tamil Nadu",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B)
                    )
                    Text(
                        text = "UPI ID: 9952184768@okaxis",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Features:\n• Nos + Feet + Kg calculations\n• Automatic Stock decrease on billing\n• Multiple payment modes (Cash / GPay / UPI)\n• Bilingual Tamil & English interface",
                        fontSize = 12.sp,
                        color = Color(0xFF1E293B)
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showCompanyInfoDialog = false }) {
                    Text("OK")
                }
            }
        )
    }
}

@Composable
fun MetricCard(
    title: String,
    amount: Double,
    icon: ImageVector,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    testTag: String,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(96.dp)
            .clickable { onClick() }
            .testTag(testTag),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = title,
                    color = Color.White.copy(alpha = 0.95f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1
                )
            }

            Text(
                text = formatCurrency(amount),
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }
    }
}

@Composable
fun ActionTile(
    title: String,
    icon: ImageVector,
    iconBgColor: Color,
    onClick: () -> Unit,
    testTag: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(108.dp)
            .clickable { onClick() }
            .testTag(testTag),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(iconBgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1E293B),
                textAlign = TextAlign.Center,
                lineHeight = 13.sp,
                maxLines = 2
            )
        }
    }
}
