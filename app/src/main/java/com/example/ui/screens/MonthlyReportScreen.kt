package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DailyStat
import com.example.ui.components.AppBottomNavBar
import com.example.ui.components.AppTopBar
import com.example.ui.components.formatCurrency
import com.example.ui.theme.BrandNavy
import com.example.ui.theme.CardPurchaseOrange
import com.example.ui.theme.TileDailyReportBlue
import com.example.util.AppStrings
import com.example.viewmodel.FabricationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthlyReportScreen(
    viewModel: FabricationViewModel
) {
    val language by viewModel.language.collectAsState()
    val activeTab by viewModel.activeTab.collectAsState()
    val monthStats by viewModel.monthStats.collectAsState()

    var selectedMonth by remember { mutableStateOf("October 2026") }
    var monthDropdownExpanded by remember { mutableStateOf(false) }

    val totalSales = 745260.0
    val totalPurchase = 412300.0
    val grossProfit = 332960.0
    val expenses = 28500.0
    val netProfit = 304460.0

    Scaffold(
        topBar = {
            AppTopBar(
                title = AppStrings.get("monthly_report", language),
                showBack = true,
                onBackClick = { viewModel.handleBack() },
                language = language,
                onLanguageToggle = { viewModel.toggleLanguage() }
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
                .verticalScroll(rememberScrollState())
                .padding(14.dp)
        ) {
            // Month Selector
            ExposedDropdownMenuBox(
                expanded = monthDropdownExpanded,
                onExpandedChange = { monthDropdownExpanded = !monthDropdownExpanded }
            ) {
                OutlinedTextField(
                    value = selectedMonth,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                        .testTag("monthly_report_month_selector"),
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = monthDropdownExpanded)
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = BrandNavy,
                        unfocusedBorderColor = Color(0xFFCBD5E1)
                    )
                )
                ExposedDropdownMenu(
                    expanded = monthDropdownExpanded,
                    onDismissRequest = { monthDropdownExpanded = false }
                ) {
                    listOf("October 2026", "September 2026", "August 2026", "July 2026").forEach { m ->
                        DropdownMenuItem(
                            text = { Text(m) },
                            onClick = {
                                selectedMonth = m
                                monthDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Metric Summary Table (Matches Screen 7 in image)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    ReportRow(label = AppStrings.get("today_sales", language).replace("Today", "Total").replace("இன்றைய", "மொத்த"), value = formatCurrency(totalSales), isBold = true)
                    HorizontalDivider(color = Color(0xFFF1F5F9), modifier = Modifier.padding(vertical = 8.dp))

                    ReportRow(label = AppStrings.get("today_purchase", language).replace("Today", "Total").replace("இன்றைய", "மொத்த"), value = formatCurrency(totalPurchase))
                    HorizontalDivider(color = Color(0xFFF1F5F9), modifier = Modifier.padding(vertical = 8.dp))

                    ReportRow(label = AppStrings.get("gross_profit", language), value = formatCurrency(grossProfit))
                    HorizontalDivider(color = Color(0xFFF1F5F9), modifier = Modifier.padding(vertical = 8.dp))

                    ReportRow(label = AppStrings.get("expenses", language), value = formatCurrency(expenses))
                    HorizontalDivider(color = Color(0xFFE2E8F0), modifier = Modifier.padding(vertical = 8.dp))

                    // Prominent Net Profit Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFDCFCE7))
                            .padding(horizontal = 12.dp, vertical = 10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = AppStrings.get("net_profit", language),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF15803D)
                            )
                            Text(
                                text = formatCurrency(netProfit),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF15803D)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Bar Chart Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    // Chart Legend
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF0284C7))
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Sales", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFF334155))

                        Spacer(modifier = Modifier.width(20.dp))

                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(CardPurchaseOrange)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Purchase", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFF334155))
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Canvas Bar Chart
                    SalesPurchaseBarChart(
                        stats = monthStats,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // X-Axis Day Labels
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        listOf("01", "05", "10", "15", "20", "25", "31").forEach { dayLabel ->
                            Text(
                                text = dayLabel,
                                fontSize = 11.sp,
                                color = Color(0xFF64748B),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SalesPurchaseBarChart(
    stats: List<DailyStat>,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val maxVal = 40000.0 // ceiling for bar height

        val daysToShow = listOf(1, 5, 10, 15, 20, 25, 31)
        val numGroups = daysToShow.size
        val groupWidth = width / numGroups
        val barWidth = (groupWidth * 0.32f).coerceAtMost(16f)

        // Draw light gridlines
        val gridLines = 4
        for (i in 0..gridLines) {
            val y = height * (i.toFloat() / gridLines)
            drawLine(
                color = Color(0xFFF1F5F9),
                start = Offset(0f, y),
                end = Offset(width, y),
                strokeWidth = 1f
            )
        }

        daysToShow.forEachIndexed { index, day ->
            val stat = stats.find { it.day == day } ?: DailyStat(day, 20000.0, 12000.0)
            val groupCenterX = (index + 0.5f) * groupWidth

            // Sales bar (Blue)
            val salesBarHeight = ((stat.sales / maxVal) * height).toFloat().coerceIn(10f, height - 10f)
            val salesX = groupCenterX - barWidth - 2f
            val salesY = height - salesBarHeight

            drawRoundRect(
                color = Color(0xFF0284C7),
                topLeft = Offset(salesX, salesY),
                size = Size(barWidth, salesBarHeight),
                cornerRadius = CornerRadius(4f, 4f)
            )

            // Purchase bar (Orange)
            val purchaseBarHeight = ((stat.purchase / maxVal) * height).toFloat().coerceIn(10f, height - 10f)
            val purchaseX = groupCenterX + 2f
            val purchaseY = height - purchaseBarHeight

            drawRoundRect(
                color = CardPurchaseOrange,
                topLeft = Offset(purchaseX, purchaseY),
                size = Size(barWidth, purchaseBarHeight),
                cornerRadius = CornerRadius(4f, 4f)
            )
        }
    }
}
