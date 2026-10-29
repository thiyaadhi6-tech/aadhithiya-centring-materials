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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Customer
import com.example.ui.components.AppBottomNavBar
import com.example.ui.components.AppTopBar
import com.example.ui.components.formatCurrency
import com.example.ui.theme.BrandNavy
import com.example.ui.theme.CardSalesGreen
import com.example.util.AppStrings
import com.example.viewmodel.FabricationViewModel

@Composable
fun CustomerBalanceScreen(
    viewModel: FabricationViewModel
) {
    val language by viewModel.language.collectAsState()
    val activeTab by viewModel.activeTab.collectAsState()
    val customers by viewModel.customers.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var showAddCustomerDialog by remember { mutableStateOf(false) }
    var selectedCustomerForPayment by remember { mutableStateOf<Customer?>(null) }

    val filteredCustomers = customers.filter {
        searchQuery.isBlank() || it.name.contains(searchQuery, ignoreCase = true) || it.phone.contains(searchQuery)
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = AppStrings.get("customer_balance", language),
                showBack = true,
                onBackClick = { viewModel.handleBack() },
                language = language,
                onLanguageToggle = { viewModel.toggleLanguage() },
                actions = {
                    IconButton(
                        onClick = { showAddCustomerDialog = true },
                        modifier = Modifier.testTag("action_add_customer")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add Customer", tint = Color.White)
                    }
                }
            )
        },
        bottomBar = {
            AppBottomNavBar(
                activeTab = activeTab,
                onTabSelected = { viewModel.selectBottomTab(it) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddCustomerDialog = true },
                containerColor = BrandNavy,
                contentColor = Color.White,
                modifier = Modifier.testTag("fab_add_customer")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Customer")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF1F5F9))
                .padding(14.dp)
        ) {
            // Search Customer Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("customer_search_input"),
                placeholder = { Text("Search customer by name...", fontSize = 13.sp, color = Color(0xFF94A3B8)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = Color(0xFF64748B)) },
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

            // Customer List
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(filteredCustomers) { cust ->
                        val hasPending = cust.pendingBalance > 0
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedCustomerForPayment = cust }
                                .padding(horizontal = 16.dp, vertical = 14.dp)
                                .testTag("customer_row_${cust.id}"),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = cust.name,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0F172A)
                                )
                                if (cust.phone.isNotBlank()) {
                                    Text(
                                        text = cust.phone,
                                        fontSize = 11.sp,
                                        color = Color(0xFF64748B)
                                    )
                                }
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = formatCurrency(cust.pendingBalance),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (hasPending) Color(0xFFDC2626) else CardSalesGreen
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = null,
                                    tint = Color(0xFF94A3B8),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        HorizontalDivider(color = Color(0xFFF1F5F9))
                    }
                }
            }
        }
    }

    // Settle / Record Payment Dialog
    selectedCustomerForPayment?.let { cust ->
        var amountText by remember { mutableStateOf(cust.pendingBalance.toInt().toString()) }

        AlertDialog(
            onDismissRequest = { selectedCustomerForPayment = null },
            title = {
                Text(
                    text = "Customer: ${cust.name}",
                    fontWeight = FontWeight.Bold,
                    color = BrandNavy
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Total Pending Balance: ${formatCurrency(cust.pendingBalance)}", fontWeight = FontWeight.SemiBold, color = Color(0xFFDC2626))
                    if (cust.phone.isNotBlank()) {
                        Text("Phone: ${cust.phone}", fontSize = 12.sp, color = Color(0xFF64748B))
                    }
                    Text("Address: ${cust.address}", fontSize = 12.sp, color = Color(0xFF64748B))

                    Spacer(modifier = Modifier.height(4.dp))

                    OutlinedTextField(
                        value = amountText,
                        onValueChange = { amountText = it },
                        label = { Text("Payment Received (₹)") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val amt = amountText.toDoubleOrNull() ?: 0.0
                        if (amt > 0) {
                            viewModel.recordPaymentReceived(cust.id, amt)
                        }
                        selectedCustomerForPayment = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CardSalesGreen)
                ) {
                    Text("Record Payment")
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedCustomerForPayment = null }) {
                    Text("Close")
                }
            }
        )
    }

    // Add Customer Dialog
    if (showAddCustomerDialog) {
        var nameText by remember { mutableStateOf("") }
        var phoneText by remember { mutableStateOf("") }
        var balanceText by remember { mutableStateOf("0") }
        var addressText by remember { mutableStateOf("Madurai") }

        AlertDialog(
            onDismissRequest = { showAddCustomerDialog = false },
            title = {
                Text(
                    text = AppStrings.get("add_customer", language),
                    fontWeight = FontWeight.Bold,
                    color = BrandNavy
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = nameText,
                        onValueChange = { nameText = it },
                        label = { Text("Customer Name / Business") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = phoneText,
                        onValueChange = { phoneText = it },
                        label = { Text("Phone Number") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = balanceText,
                        onValueChange = { balanceText = it },
                        label = { Text("Initial Credit Balance (₹)") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = addressText,
                        onValueChange = { addressText = it },
                        label = { Text("Area / Location") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (nameText.isNotBlank()) {
                            val initBal = balanceText.toDoubleOrNull() ?: 0.0
                            viewModel.addNewCustomer(nameText, phoneText, initBal, addressText)
                            showAddCustomerDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandNavy)
                ) {
                    Text("Save Customer")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddCustomerDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
