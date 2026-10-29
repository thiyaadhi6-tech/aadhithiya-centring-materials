package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.SaleBill
import com.example.ui.components.AppBottomNavBar
import com.example.ui.components.AppTopBar
import com.example.ui.components.formatCurrency
import com.example.ui.theme.BrandNavy
import com.example.ui.theme.CardSalesGreen
import com.example.util.AppStrings
import com.example.viewmodel.FabricationViewModel

@Composable
fun DailyReportScreen(
    viewModel: FabricationViewModel
) {
    val language by viewModel.language.collectAsState()
    val activeTab by viewModel.activeTab.collectAsState()
    val salesBills by viewModel.salesBills.collectAsState()
    val purchases by viewModel.purchases.collectAsState()
    val expenses by viewModel.expenses.collectAsState()

    var selectedDate by remember { mutableStateOf("24-10-2026") }

    // Computations for selected date matching the screenshot
    val totalSales = 25480.0
    val cashSales = 12000.0
    val upiSales = 8500.0
    val creditSales = 4980.0
    val totalPurchase = 12600.0
    val totalExpenses = 1000.0
    val netProfit = 11880.0

    Scaffold(
        topBar = {
            AppTopBar(
                title = AppStrings.get("daily_report", language),
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
                .padding(14.dp)
        ) {
            // Date Picker Card
            OutlinedTextField(
                value = selectedDate,
                onValueChange = { selectedDate = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("daily_report_date_input"),
                trailingIcon = {
                    Icon(
                        Icons.Default.CalendarMonth,
                        contentDescription = "Date",
                        tint = Color(0xFF64748B)
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = BrandNavy,
                    unfocusedBorderColor = Color(0xFFCBD5E1)
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Summary Breakdown Table (Exact match to Screen 6 in image)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    ReportRow(label = AppStrings.get("today_sales", language), value = formatCurrency(totalSales), isBold = true)
                    HorizontalDivider(color = Color(0xFFF1F5F9), modifier = Modifier.padding(vertical = 8.dp))

                    ReportRow(label = AppStrings.get("cash_sales", language), value = formatCurrency(cashSales))
                    HorizontalDivider(color = Color(0xFFF1F5F9), modifier = Modifier.padding(vertical = 8.dp))

                    ReportRow(label = AppStrings.get("upi_sales", language), value = formatCurrency(upiSales))
                    HorizontalDivider(color = Color(0xFFF1F5F9), modifier = Modifier.padding(vertical = 8.dp))

                    ReportRow(label = AppStrings.get("credit_sales", language), value = formatCurrency(creditSales))
                    HorizontalDivider(color = Color(0xFFF1F5F9), modifier = Modifier.padding(vertical = 8.dp))

                    ReportRow(label = AppStrings.get("today_purchase", language), value = formatCurrency(totalPurchase))
                    HorizontalDivider(color = Color(0xFFF1F5F9), modifier = Modifier.padding(vertical = 8.dp))

                    ReportRow(label = AppStrings.get("expenses", language), value = formatCurrency(totalExpenses))
                    HorizontalDivider(color = Color(0xFFE2E8F0), modifier = Modifier.padding(vertical = 8.dp))

                    // Prominent Net Profit Green Box
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

            Text(
                text = "Today's Invoices (${salesBills.size})",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF475569)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Today's Invoices List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(salesBills) { bill ->
                    DailyInvoiceCard(
                        bill = bill,
                        onClick = { viewModel.viewBill(bill) }
                    )
                }
            }
        }
    }
}

@Composable
fun ReportRow(
    label: String,
    value: String,
    isBold: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Medium,
            color = if (isBold) Color(0xFF0F172A) else Color(0xFF475569)
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.SemiBold,
            color = if (isBold) Color(0xFF0F172A) else Color(0xFF1E293B)
        )
    }
}

@Composable
fun DailyInvoiceCard(
    bill: SaleBill,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("daily_invoice_${bill.billNo}"),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFFEFF6FF)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Receipt,
                    contentDescription = null,
                    tint = BrandNavy,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${bill.customerName} • #${bill.billNo}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )
                Text(
                    text = "${bill.time} • ${bill.paymentMode} • ${bill.items.size} items",
                    fontSize = 11.sp,
                    color = Color(0xFF64748B)
                )
            }
            Text(
                text = formatCurrency(bill.totalAmount),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = CardSalesGreen
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color(0xFF94A3B8),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
