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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Hardware
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
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
import com.example.model.InventoryItem
import com.example.model.UnitType
import com.example.ui.components.AppBottomNavBar
import com.example.ui.components.AppTopBar
import com.example.ui.theme.BrandNavy
import com.example.ui.theme.BrandOrange
import com.example.util.AppStrings
import com.example.viewmodel.FabricationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockManagementScreen(
    viewModel: FabricationViewModel
) {
    val language by viewModel.language.collectAsState()
    val activeTab by viewModel.activeTab.collectAsState()
    val inventory by viewModel.inventory.collectAsState()
    val filterUnit by viewModel.stockFilterUnit.collectAsState()
    val searchQuery by viewModel.stockSearchQuery.collectAsState()

    var showAddProductDialog by remember { mutableStateOf(false) }
    var selectedItemForEdit by remember { mutableStateOf<InventoryItem?>(null) }

    val filteredItems = inventory.filter { item ->
        val matchesFilter = (filterUnit == null || item.unit == filterUnit)
        val matchesSearch = searchQuery.isBlank() ||
                item.name.contains(searchQuery, ignoreCase = true) ||
                item.tamilName.contains(searchQuery, ignoreCase = true)
        matchesFilter && matchesSearch
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = AppStrings.get("stock_management", language),
                showBack = true,
                onBackClick = { viewModel.handleBack() },
                language = language,
                onLanguageToggle = { viewModel.toggleLanguage() },
                actions = {
                    IconButton(
                        onClick = { showAddProductDialog = true },
                        modifier = Modifier.testTag("action_add_product")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add Item", tint = Color.White)
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
                onClick = { showAddProductDialog = true },
                containerColor = BrandNavy,
                contentColor = Color.White,
                modifier = Modifier.testTag("fab_add_product")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Item")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF1F5F9))
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            // Category Filter Pills: [All Items] [Nos] [Feet] [Kg]
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                UnitFilterChip(
                    label = AppStrings.get("all_items", language),
                    isSelected = filterUnit == null,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.setStockFilterUnit(null) }
                )
                UnitFilterChip(
                    label = "Nos",
                    isSelected = filterUnit == UnitType.NOS,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.setStockFilterUnit(UnitType.NOS) }
                )
                UnitFilterChip(
                    label = "Feet",
                    isSelected = filterUnit == UnitType.FEET,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.setStockFilterUnit(UnitType.FEET) }
                )
                UnitFilterChip(
                    label = "Kg",
                    isSelected = filterUnit == UnitType.KG,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.setStockFilterUnit(UnitType.KG) }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setStockSearchQuery(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("stock_search_input"),
                placeholder = { Text(AppStrings.get("search_item", language), fontSize = 13.sp, color = Color(0xFF94A3B8)) },
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

            Spacer(modifier = Modifier.height(10.dp))

            // Stock Items List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredItems) { item ->
                    StockItemCard(
                        item = item,
                        onClick = { selectedItemForEdit = item }
                    )
                }
            }
        }
    }

    // Add Product Dialog
    if (showAddProductDialog) {
        var nameText by remember { mutableStateOf("") }
        var tamilNameText by remember { mutableStateOf("") }
        var unitSelected by remember { mutableStateOf(UnitType.NOS) }
        var stockText by remember { mutableStateOf("100") }
        var rateText by remember { mutableStateOf("150") }

        AlertDialog(
            onDismissRequest = { showAddProductDialog = false },
            title = {
                Text(
                    text = AppStrings.get("add_item", language),
                    fontWeight = FontWeight.Bold,
                    color = BrandNavy
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = nameText,
                        onValueChange = { nameText = it },
                        label = { Text("Product Name (English)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = tamilNameText,
                        onValueChange = { tamilNameText = it },
                        label = { Text("பொருள் பெயர் (Tamil)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Text("Unit Type:", fontSize = 12.sp, color = Color(0xFF64748B))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        UnitType.values().forEach { u ->
                            val isSel = unitSelected == u
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (isSel) BrandNavy else Color(0xFFE2E8F0))
                                    .clickable { unitSelected = u }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = u.label,
                                    fontSize = 12.sp,
                                    color = if (isSel) Color.White else Color(0xFF334155),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = stockText,
                            onValueChange = { stockText = it },
                            label = { Text("Initial Stock") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = rateText,
                            onValueChange = { rateText = it },
                            label = { Text("Selling Rate (₹)") },
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
                        if (nameText.isNotBlank()) {
                            val st = stockText.toDoubleOrNull() ?: 0.0
                            val rt = rateText.toDoubleOrNull() ?: 0.0
                            viewModel.addNewProduct(nameText, tamilNameText, unitSelected, st, rt)
                            showAddProductDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandNavy)
                ) {
                    Text("Add Product")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddProductDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Edit Item Dialog
    selectedItemForEdit?.let { item ->
        var editStockText by remember { mutableStateOf(item.currentStock.toInt().toString()) }
        var editRateText by remember { mutableStateOf(item.unitRate.toInt().toString()) }

        AlertDialog(
            onDismissRequest = { selectedItemForEdit = null },
            title = {
                Text(
                    text = "Update ${item.name}",
                    fontWeight = FontWeight.Bold,
                    color = BrandNavy
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Unit: ${item.unit.label} (${item.unit.tamilLabel})", fontSize = 13.sp, color = Color(0xFF64748B))
                    OutlinedTextField(
                        value = editStockText,
                        onValueChange = { editStockText = it },
                        label = { Text("Current Stock (${item.unit.label})") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = editRateText,
                        onValueChange = { editRateText = it },
                        label = { Text("Selling Rate (₹ per ${item.unit.label})") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val st = editStockText.toDoubleOrNull() ?: item.currentStock
                        val rt = editRateText.toDoubleOrNull() ?: item.unitRate
                        viewModel.updateStock(item.id, st, rt)
                        selectedItemForEdit = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandNavy)
                ) {
                    Text("Save Changes")
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedItemForEdit = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun UnitFilterChip(
    label: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(36.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) Color(0xFF1D4ED8) else Color.White)
            .border(
                1.dp,
                if (isSelected) Color(0xFF1D4ED8) else Color(0xFFCBD5E1),
                RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
            .testTag("filter_chip_$label"),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) Color.White else Color(0xFF334155)
        )
    }
}

@Composable
fun StockItemCard(
    item: InventoryItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("stock_item_${item.id}"),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Industrial Product Icon Container
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFEFF6FF)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Hardware,
                    contentDescription = item.name,
                    tint = BrandNavy,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Stock: ${item.currentStock.toInt()} ${item.unit.label}",
                    fontSize = 12.sp,
                    color = Color(0xFF64748B)
                )
                Text(
                    text = "Rate: ₹${item.unitRate.toInt()}",
                    fontSize = 12.sp,
                    color = Color(0xFF64748B)
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Edit",
                tint = Color(0xFF94A3B8),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
