package com.example.model

enum class UnitType(val label: String, val tamilLabel: String) {
    NOS("Nos", "எண்கள்"),
    FEET("Feet", "அடி"),
    KG("Kg", "கிலோ")
}

data class InventoryItem(
    val id: String,
    val name: String,
    val tamilName: String = "",
    val unit: UnitType,
    val currentStock: Double,
    val unitRate: Double,
    val category: String = "Scaffolding"
)

data class BillItem(
    val id: String,
    val itemId: String,
    val itemName: String,
    val unit: String,
    val qty: Double,
    val rate: Double,
    val amount: Double
)

data class SaleBill(
    val id: String,
    val billNo: String,
    val customerName: String,
    val customerPhone: String = "",
    val date: String,
    val time: String,
    val items: List<BillItem>,
    val totalAmount: Double,
    val paymentMode: String, // "Cash", "GPay", "UPI", "Credit"
    val timestamp: Long = System.currentTimeMillis()
)

data class PurchaseEntry(
    val id: String,
    val supplierName: String,
    val date: String,
    val items: List<BillItem>,
    val totalAmount: Double,
    val paymentType: String, // "Cash", "Bank", "UPI", "Credit"
    val timestamp: Long = System.currentTimeMillis()
)

data class Customer(
    val id: String,
    val name: String,
    val phone: String,
    val pendingBalance: Double,
    val address: String = "Madurai"
)

data class Expense(
    val id: String,
    val title: String,
    val amount: Double,
    val date: String,
    val category: String
)

data class DailyStat(
    val day: Int,
    val sales: Double,
    val purchase: Double
)

enum class AppScreen {
    SPLASH,
    DASHBOARD,
    COUNTER_SALES,
    STOCK_MANAGEMENT,
    PURCHASE_ENTRY,
    DAILY_REPORT,
    MONTHLY_REPORT,
    CUSTOMER_BALANCE,
    PROFIT_LOSS,
    SALES_BILL_VIEW
}

enum class Language {
    ENGLISH,
    TAMIL
}
