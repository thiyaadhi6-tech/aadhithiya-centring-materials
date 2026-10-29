package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.InventoryItem
import com.example.ui.components.AppTopBar
import com.example.ui.components.formatCurrency
import com.example.ui.theme.BrandNavy
import com.example.ui.theme.CardSalesGreen
import com.example.ui.theme.GPayBlue
import com.example.ui.theme.TileSalesPurple
import com.example.ui.theme.UpiGreen
import com.example.util.AppStrings
import com.example.viewmodel.FabricationViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CounterSalesScreen(
    viewModel: FabricationViewModel
) {
    val language by viewModel.language.collectAsState()
    val draftCustomer by viewModel.draftCustomerName.collectAsState()
    val draftDate by viewModel.draftSaleDate.collectAsState()
    val draftItems by viewModel.draftSaleItems.collectAsState()
    val paymentMode by viewModel.draftPaymentMode.collectAsState()
    val inventory by viewModel.inventory.collectAsState()
    val customers by viewModel.customers.collectAsState()

    var showAddItemDialog by remember { mutableStateOf(false) }
    var customerDropdownExpanded by remember { mutableStateOf(false) }

    val totalAmount = draftItems.sumOf { it.amount }

    Scaffold(
        topBar = {
            AppTopBar(
                title = AppStrings.get("counter_sales", language),
                showBack = true,
                onBackClick = { viewModel.handleBack() },
                language = language,
                onLanguageToggle = { viewModel.toggleLanguage() }
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
            // Customer Name & Date Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    // Customer Name Selector
                    Text(
                        text = AppStrings.get("customer_name", language),
                        fontSize = 11.sp,
                        color = Color(0xFF64748B),
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    ExposedDropdownMenuBox(
                        expanded = customerDropdownExpanded,
                        onExpandedChange = { customerDropdownExpanded = !customerDropdownExpanded }
                    ) {
                        OutlinedTextField(
                            value = draftCustomer,
                            onValueChange = { viewModel.setDraftCustomer(it) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                                .testTag("input_customer_name"),
                            trailingIcon = {
                                Icon(
                                    Icons.Default.Search,
                                    contentDescription = "Search Customer",
                                    tint = Color(0xFF64748B)
                                )
                            },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = BrandNavy,
                                unfocusedBorderColor = Color(0xFFCBD5E1)
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = customerDropdownExpanded,
                            onDismissRequest = { customerDropdownExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Walk-in Customer") },
                                onClick = {
                                    viewModel.setDraftCustomer("Walk-in Customer")
                                    customerDropdownExpanded = false
                                }
                            )
                            customers.forEach { cust ->
                                DropdownMenuItem(
                                    text = { Text("${cust.name} (Due: ₹${cust.pendingBalance.toInt()})") },
                                    onClick = {
                                        viewModel.setDraftCustomer(cust.name)
                                        customerDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Date
                    Text(
                        text = AppStrings.get("date", language),
                        fontSize = 11.sp,
                        color = Color(0xFF64748B),
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = draftDate,
                        onValueChange = { viewModel.setDraftSaleDate(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_sale_date"),
                        trailingIcon = {
                            Icon(
                                Icons.Default.CalendarMonth,
                                contentDescription = "Date",
                                tint = Color(0xFF64748B)
                            )
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BrandNavy,
                            unfocusedBorderColor = Color(0xFFCBD5E1)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Quick Add Item Bar
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showAddItemDialog = true }
                    .testTag("quick_add_item_bar"),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Search Item",
                        tint = Color(0xFF94A3B8),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = AppStrings.get("search_item", language),
                        color = Color(0xFF94A3B8),
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFEFF6FF))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Add, contentDescription = "Add", tint = BrandNavy, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Add Item", fontSize = 12.sp, color = BrandNavy, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Items Table
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Table Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF8FAFC))
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = AppStrings.get("item", language),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF475569),
                            modifier = Modifier.weight(2.5f)
                        )
                        Text(
                            text = AppStrings.get("qty", language),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF475569),
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = AppStrings.get("rate", language),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF475569),
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.End
                        )
                        Text(
                            text = AppStrings.get("amount", language),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF475569),
                            modifier = Modifier.weight(1.5f),
                            textAlign = TextAlign.End
                        )
                        Spacer(modifier = Modifier.width(24.dp))
                    }

                    HorizontalDivider(color = Color(0xFFE2E8F0))

                    // Items List
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        itemsIndexed(draftItems) { index, item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = item.itemName,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF1E293B),
                                    modifier = Modifier.weight(2.5f)
                                )
                                Text(
                                    text = item.qty.toInt().toString(),
                                    fontSize = 12.sp,
                                    color = Color(0xFF334155),
                                    modifier = Modifier.weight(1f),
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = item.rate.toInt().toString(),
                                    fontSize = 12.sp,
                                    color = Color(0xFF334155),
                                    modifier = Modifier.weight(1f),
                                    textAlign = TextAlign.End
                                )
                                Text(
                                    text = NumberFormat.getNumberInstance(Locale("en", "IN")).format(item.amount.toInt()),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0F172A),
                                    modifier = Modifier.weight(1.5f),
                                    textAlign = TextAlign.End
                                )
                                IconButton(
                                    onClick = { viewModel.removeDraftSaleItem(index) },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = "Remove",
                                        tint = Color(0xFFEF4444),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                            HorizontalDivider(color = Color(0xFFF1F5F9))
                        }
                    }

                    // Total Row
                    HorizontalDivider(color = Color(0xFFE2E8F0))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF8FAFC))
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = AppStrings.get("total_amount", language),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Text(
                            text = NumberFormat.getNumberInstance(Locale("en", "IN")).format(totalAmount.toInt()),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF0F172A)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Payment Mode Pill Selector: [Cash] [GPay] [UPI] [Credit]
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PaymentModePill(
                    label = "Cash",
                    isSelected = paymentMode.equals("Cash", ignoreCase = true),
                    selectedColor = CardSalesGreen,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.setDraftPaymentMode("Cash") }
                )
                PaymentModePill(
                    label = "GPay",
                    isSelected = paymentMode.equals("GPay", ignoreCase = true),
                    selectedColor = TileSalesPurple,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.setDraftPaymentMode("GPay") }
                )
                PaymentModePill(
                    label = "UPI",
                    isSelected = paymentMode.equals("UPI", ignoreCase = true),
                    selectedColor = Color(0xFF0284C7),
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.setDraftPaymentMode("UPI") }
                )
                PaymentModePill(
                    label = "Credit",
                    isSelected = paymentMode.equals("Credit", ignoreCase = true),
                    selectedColor = Color(0xFFDC2626),
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.setDraftPaymentMode("Credit") }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Save Bill Button
            Button(
                onClick = { viewModel.saveDraftBill() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("save_bill_button"),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1D4ED8)
                )
            ) {
                Text(
                    text = AppStrings.get("save_bill", language),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }

    // Add Item Dialog
    if (showAddItemDialog) {
        var selectedItem by remember { mutableStateOf(inventory.firstOrNull()) }
        var qtyText by remember { mutableStateOf("10") }
        var customRateText by remember { mutableStateOf(selectedItem?.unitRate?.toInt()?.toString() ?: "100") }

        AlertDialog(
            onDismissRequest = { showAddItemDialog = false },
            title = {
                Text(
                    text = "Add Item to Bill",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = BrandNavy
                )
            },
            text = {
                Column {
                    Text("Select Product:", fontSize = 12.sp, color = Color(0xFF64748B))
                    Spacer(modifier = Modifier.height(4.dp))
                    LazyColumn(modifier = Modifier.height(140.dp)) {
                        items(inventory.size) { i ->
                            val invItem = inventory[i]
                            val isSel = selectedItem?.id == invItem.id
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (isSel) Color(0xFFDBEAFE) else Color.Transparent)
                                    .clickable {
                                        selectedItem = invItem
                                        customRateText = invItem.unitRate.toInt().toString()
                                    }
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "${invItem.name} (${invItem.unit.label})",
                                    fontSize = 13.sp,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                                )
                                Text(
                                    text = "₹${invItem.unitRate.toInt()}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = BrandNavy
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = qtyText,
                            onValueChange = { qtyText = it },
                            label = { Text("Quantity") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = customRateText,
                            onValueChange = { customRateText = it },
                            label = { Text("Rate (₹)") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val item = selectedItem
                        val qty = qtyText.toDoubleOrNull() ?: 1.0
                        val rate = customRateText.toDoubleOrNull() ?: (item?.unitRate ?: 100.0)
                        if (item != null) {
                            viewModel.addDraftSaleItem(item, qty, rate)
                        }
                        showAddItemDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandNavy)
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddItemDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun PaymentModePill(
    label: String,
    isSelected: Boolean,
    selectedColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(42.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) selectedColor else Color.White)
            .border(
                1.dp,
                if (isSelected) selectedColor else Color(0xFFCBD5E1),
                RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
            .testTag("payment_mode_$label"),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) Color.White else Color(0xFF334155)
        )
    }
}
