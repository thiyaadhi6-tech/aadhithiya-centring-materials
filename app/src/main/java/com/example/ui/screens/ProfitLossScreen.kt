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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AppBottomNavBar
import com.example.ui.components.AppTopBar
import com.example.ui.components.formatCurrency
import com.example.ui.theme.BrandNavy
import com.example.ui.theme.CardPendingRed
import com.example.ui.theme.CardPurchaseOrange
import com.example.ui.theme.CardSalesGreen
import com.example.ui.theme.TileDailyReportBlue
import com.example.util.AppStrings
import com.example.viewmodel.FabricationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfitLossScreen(
    viewModel: FabricationViewModel
) {
    val language by viewModel.language.collectAsState()
    val activeTab by viewModel.activeTab.collectAsState()

    var selectedMonth by remember { mutableStateOf("October 2026") }
    var monthDropdownExpanded by remember { mutableStateOf(false) }

    val totalSales = 745260.0
    val totalPurchase = 412300.0
    val totalExpenses = 28500.0
    val netProfit = 304460.0

    Scaffold(
        topBar = {
            AppTopBar(
                title = AppStrings.get("profit_loss", language),
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
                        .testTag("profit_loss_month_selector"),
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
                    listOf("October 2026", "September 2026", "August 2026").forEach { m ->
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

            // Profit & Loss Table (Exact match to Screen 9 in image)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    ReportRow(label = "Total Sales", value = formatCurrency(totalSales), isBold = true)
                    HorizontalDivider(color = Color(0xFFF1F5F9), modifier = Modifier.padding(vertical = 8.dp))

                    ReportRow(label = "Total Purchase", value = formatCurrency(totalPurchase))
                    HorizontalDivider(color = Color(0xFFF1F5F9), modifier = Modifier.padding(vertical = 8.dp))

                    ReportRow(label = "Total Expenses", value = formatCurrency(totalExpenses))
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

            Spacer(modifier = Modifier.height(16.dp))

            // Pie / Donut Chart Card (Exact match to Screen 9 in image)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Pie Chart Canvas
                    Box(
                        modifier = Modifier
                            .size(140.dp)
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val canvasSize = size.minDimension
                            val radius = canvasSize / 2f
                            val center = Offset(size.width / 2f, size.height / 2f)

                            // Slices:
                            // Sales: 45% -> 162 deg (Blue)
                            // Purchase: 25% -> 90 deg (Orange)
                            // Expenses: 5% -> 18 deg (Red)
                            // Profit: 25% -> 90 deg (Green)
                            val slices = listOf(
                                Triple(162f, Color(0xFF0284C7), 0f),
                                Triple(90f, CardPurchaseOrange, 162f),
                                Triple(18f, Color(0xFFEF4444), 252f),
                                Triple(90f, CardSalesGreen, 270f)
                            )

                            slices.forEach { (sweep, color, start) ->
                                drawArc(
                                    color = color,
                                    startAngle = start,
                                    sweepAngle = sweep,
                                    useCenter = true,
                                    topLeft = Offset(center.x - radius, center.y - radius),
                                    size = Size(radius * 2, radius * 2)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(20.dp))

                    // Legend (Sales 45%, Purchase 25%, Expenses 5%, Profit 25%)
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ChartLegendItem(color = Color(0xFF0284C7), label = "Sales", percentage = "45%")
                        ChartLegendItem(color = CardPurchaseOrange, label = "Purchase", percentage = "25%")
                        ChartLegendItem(color = Color(0xFFEF4444), label = "Expenses", percentage = "5%")
                        ChartLegendItem(color = CardSalesGreen, label = "Profit", percentage = "25%")
                    }
                }
            }
        }
    }
}

@Composable
fun ChartLegendItem(
    color: Color,
    label: String,
    percentage: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                fontSize = 13.sp,
                color = Color(0xFF334155),
                fontWeight = FontWeight.Medium
            )
        }
        Text(
            text = percentage,
            fontSize = 13.sp,
            color = Color(0xFF0F172A),
            fontWeight = FontWeight.Bold
        )
    }
}
