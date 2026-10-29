package com.example.viewmodel

import androidx.lifecycle.ViewModel
import com.example.data.SampleData
import com.example.model.AppScreen
import com.example.model.BillItem
import com.example.model.Customer
import com.example.model.DailyStat
import com.example.model.Expense
import com.example.model.InventoryItem
import com.example.model.Language
import com.example.model.PurchaseEntry
import com.example.model.SaleBill
import com.example.model.UnitType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FabricationViewModel : ViewModel() {

    private val _currentScreen = MutableStateFlow(AppScreen.SPLASH)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val screenStack = mutableListOf<AppScreen>(AppScreen.SPLASH)

    private val _activeTab = MutableStateFlow(0)
    val activeTab: StateFlow<Int> = _activeTab.asStateFlow()

    private val _language = MutableStateFlow(Language.ENGLISH)
    val language: StateFlow<Language> = _language.asStateFlow()

    private val _inventory = MutableStateFlow(SampleData.initialInventory)
    val inventory: StateFlow<List<InventoryItem>> = _inventory.asStateFlow()

    private val _salesBills = MutableStateFlow(SampleData.initialSalesBills)
    val salesBills: StateFlow<List<SaleBill>> = _salesBills.asStateFlow()

    private val _purchases = MutableStateFlow(SampleData.initialPurchases)
    val purchases: StateFlow<List<PurchaseEntry>> = _purchases.asStateFlow()

    private val _customers = MutableStateFlow(SampleData.initialCustomers)
    val customers: StateFlow<List<Customer>> = _customers.asStateFlow()

    private val _expenses = MutableStateFlow(SampleData.initialExpenses)
    val expenses: StateFlow<List<Expense>> = _expenses.asStateFlow()

    private val _monthStats = MutableStateFlow(SampleData.initialMonthStats)
    val monthStats: StateFlow<List<DailyStat>> = _monthStats.asStateFlow()

    private val _selectedBill = MutableStateFlow<SaleBill?>(SampleData.sampleBill00154)
    val selectedBill: StateFlow<SaleBill?> = _selectedBill.asStateFlow()

    // Counter Sales Draft State
    private val _draftCustomerName = MutableStateFlow("Walk-in Customer")
    val draftCustomerName: StateFlow<String> = _draftCustomerName.asStateFlow()

    private val _draftSaleDate = MutableStateFlow("24-10-2026")
    val draftSaleDate: StateFlow<String> = _draftSaleDate.asStateFlow()

    private val _draftSaleItems = MutableStateFlow<List<BillItem>>(
        listOf(
            BillItem("d_1", "item_1", "Adjustable Jack (Nos)", "Nos", 10.0, 250.0, 2500.0),
            BillItem("d_2", "item_2", "Jockey (Nos)", "Nos", 20.0, 150.0, 3000.0),
            BillItem("d_3", "item_3", "Pipe (Feet)", "Feet", 50.0, 120.0, 6000.0),
            BillItem("d_4", "item_4", "Clamp (Nos)", "Nos", 10.0, 80.0, 800.0)
        )
    )
    val draftSaleItems: StateFlow<List<BillItem>> = _draftSaleItems.asStateFlow()

    private val _draftPaymentMode = MutableStateFlow("Cash") // Cash, GPay, UPI, Credit
    val draftPaymentMode: StateFlow<String> = _draftPaymentMode.asStateFlow()

    // Purchase Entry Draft State
    private val _draftSupplierName = MutableStateFlow("ABC Steel Traders")
    val draftSupplierName: StateFlow<String> = _draftSupplierName.asStateFlow()

    private val _draftPurchaseDate = MutableStateFlow("24-10-2026")
    val draftPurchaseDate: StateFlow<String> = _draftPurchaseDate.asStateFlow()

    private val _draftPurchaseItems = MutableStateFlow<List<BillItem>>(
        listOf(
            BillItem("dp_1", "item_3", "Pipe (Feet)", "Feet", 500.0, 110.0, 55000.0),
            BillItem("dp_2", "item_4", "Clamp (Nos)", "Nos", 100.0, 70.0, 7000.0),
            BillItem("dp_3", "item_2", "Jockey (Nos)", "Nos", 200.0, 130.0, 26000.0)
        )
    )
    val draftPurchaseItems: StateFlow<List<BillItem>> = _draftPurchaseItems.asStateFlow()

    private val _draftPurchasePaymentType = MutableStateFlow("Cash")
    val draftPurchasePaymentType: StateFlow<String> = _draftPurchasePaymentType.asStateFlow()

    // Stock Filter & Search
    private val _stockFilterUnit = MutableStateFlow<UnitType?>(null) // null = All Items
    val stockFilterUnit: StateFlow<UnitType?> = _stockFilterUnit.asStateFlow()

    private val _stockSearchQuery = MutableStateFlow("")
    val stockSearchQuery: StateFlow<String> = _stockSearchQuery.asStateFlow()

    // UI feedback toast/snack message
    private val _feedbackMessage = MutableStateFlow<String?>(null)
    val feedbackMessage: StateFlow<String?> = _feedbackMessage.asStateFlow()

    fun navigateTo(screen: AppScreen) {
        if (_currentScreen.value != screen) {
            screenStack.add(screen)
            _currentScreen.value = screen
        }
        when (screen) {
            AppScreen.DASHBOARD -> _activeTab.value = 0
            AppScreen.COUNTER_SALES -> _activeTab.value = 1
            AppScreen.STOCK_MANAGEMENT -> _activeTab.value = 2
            AppScreen.DAILY_REPORT, AppScreen.MONTHLY_REPORT, AppScreen.CUSTOMER_BALANCE, AppScreen.PROFIT_LOSS -> _activeTab.value = 3
            else -> {}
        }
    }

    fun handleBack(): Boolean {
        if (screenStack.size > 1) {
            screenStack.removeAt(screenStack.size - 1)
            val previous = screenStack.last()
            _currentScreen.value = previous
            when (previous) {
                AppScreen.DASHBOARD -> _activeTab.value = 0
                AppScreen.COUNTER_SALES -> _activeTab.value = 1
                AppScreen.STOCK_MANAGEMENT -> _activeTab.value = 2
                else -> {}
            }
            return true
        }
        return false
    }

    fun selectBottomTab(tabIndex: Int) {
        _activeTab.value = tabIndex
        when (tabIndex) {
            0 -> navigateTo(AppScreen.DASHBOARD)
            1 -> navigateTo(AppScreen.COUNTER_SALES)
            2 -> navigateTo(AppScreen.STOCK_MANAGEMENT)
            3 -> navigateTo(AppScreen.MONTHLY_REPORT)
        }
    }

    fun toggleLanguage() {
        _language.value = if (_language.value == Language.ENGLISH) Language.TAMIL else Language.ENGLISH
    }

    fun clearFeedback() {
        _feedbackMessage.value = null
    }

    // Counter Sales actions
    fun setDraftCustomer(customerName: String) {
        _draftCustomerName.value = customerName
    }

    fun setDraftSaleDate(date: String) {
        _draftSaleDate.value = date
    }

    fun setDraftPaymentMode(mode: String) {
        _draftPaymentMode.value = mode
    }

    fun addDraftSaleItem(item: InventoryItem, qty: Double, customRate: Double? = null) {
        val rate = customRate ?: item.unitRate
        val amount = qty * rate
        val displayName = "${item.name} (${item.unit.label})"
        val newItem = BillItem(
            id = "di_${System.currentTimeMillis()}",
            itemId = item.id,
            itemName = displayName,
            unit = item.unit.label,
            qty = qty,
            rate = rate,
            amount = amount
        )
        _draftSaleItems.value = _draftSaleItems.value + newItem
    }

    fun removeDraftSaleItem(index: Int) {
        if (index in _draftSaleItems.value.indices) {
            val list = _draftSaleItems.value.toMutableList()
            list.removeAt(index)
            _draftSaleItems.value = list
        }
    }

    fun saveDraftBill() {
        val items = _draftSaleItems.value
        if (items.isEmpty()) {
            _feedbackMessage.value = "Please add at least one item to save bill"
            return
        }
        val total = items.sumOf { it.amount }
        val nextNum = 154 + _salesBills.value.size - 3
        val billNo = String.format(Locale.getDefault(), "%05d", nextNum)
        val currentTime = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())

        val newBill = SaleBill(
            id = "bill_${System.currentTimeMillis()}",
            billNo = billNo,
            customerName = _draftCustomerName.value,
            date = _draftSaleDate.value,
            time = currentTime,
            items = items,
            totalAmount = total,
            paymentMode = _draftPaymentMode.value
        )

        // 1. Deduct stock automatically
        val currentInv = _inventory.value.toMutableList()
        for (bi in items) {
            val index = currentInv.indexOfFirst { it.id == bi.itemId || it.name in bi.itemName }
            if (index != -1) {
                val existing = currentInv[index]
                val updatedStock = (existing.currentStock - bi.qty).coerceAtLeast(0.0)
                currentInv[index] = existing.copy(currentStock = updatedStock)
            }
        }
        _inventory.value = currentInv

        // 2. If credit, update customer pending balance
        if (_draftPaymentMode.value.equals("Credit", ignoreCase = true)) {
            val custList = _customers.value.toMutableList()
            val cIdx = custList.indexOfFirst { it.name.equals(_draftCustomerName.value, ignoreCase = true) }
            if (cIdx != -1) {
                val c = custList[cIdx]
                custList[cIdx] = c.copy(pendingBalance = c.pendingBalance + total)
            } else if (_draftCustomerName.value != "Walk-in Customer") {
                custList.add(Customer("cust_${System.currentTimeMillis()}", _draftCustomerName.value, "", total))
            }
            _customers.value = custList
        }

        // 3. Add to sales bills
        _salesBills.value = listOf(newBill) + _salesBills.value
        _selectedBill.value = newBill
        _feedbackMessage.value = "Bill #$billNo saved successfully! Stock updated."

        // Navigate to invoice preview
        navigateTo(AppScreen.SALES_BILL_VIEW)
    }

    // Purchase Entry actions
    fun setDraftSupplier(supplier: String) {
        _draftSupplierName.value = supplier
    }

    fun setDraftPurchaseDate(date: String) {
        _draftPurchaseDate.value = date
    }

    fun setDraftPurchasePaymentType(type: String) {
        _draftPurchasePaymentType.value = type
    }

    fun addDraftPurchaseItem(item: InventoryItem, qty: Double, customRate: Double) {
        val amount = qty * customRate
        val displayName = "${item.name} (${item.unit.label})"
        val newItem = BillItem(
            id = "dpi_${System.currentTimeMillis()}",
            itemId = item.id,
            itemName = displayName,
            unit = item.unit.label,
            qty = qty,
            rate = customRate,
            amount = amount
        )
        _draftPurchaseItems.value = _draftPurchaseItems.value + newItem
    }

    fun removeDraftPurchaseItem(index: Int) {
        if (index in _draftPurchaseItems.value.indices) {
            val list = _draftPurchaseItems.value.toMutableList()
            list.removeAt(index)
            _draftPurchaseItems.value = list
        }
    }

    fun saveDraftPurchase() {
        val items = _draftPurchaseItems.value
        if (items.isEmpty()) {
            _feedbackMessage.value = "Please add items to save purchase entry"
            return
        }
        val total = items.sumOf { it.amount }
        val newEntry = PurchaseEntry(
            id = "pur_${System.currentTimeMillis()}",
            supplierName = _draftSupplierName.value,
            date = _draftPurchaseDate.value,
            items = items,
            totalAmount = total,
            paymentType = _draftPurchasePaymentType.value
        )

        // Increase inventory stock
        val currentInv = _inventory.value.toMutableList()
        for (bi in items) {
            val index = currentInv.indexOfFirst { it.id == bi.itemId || it.name in bi.itemName }
            if (index != -1) {
                val existing = currentInv[index]
                currentInv[index] = existing.copy(currentStock = existing.currentStock + bi.qty)
            }
        }
        _inventory.value = currentInv
        _purchases.value = listOf(newEntry) + _purchases.value
        _feedbackMessage.value = "Purchase recorded! Stock added to inventory."
        navigateTo(AppScreen.STOCK_MANAGEMENT)
    }

    // Stock Management actions
    fun setStockFilterUnit(unit: UnitType?) {
        _stockFilterUnit.value = unit
    }

    fun setStockSearchQuery(query: String) {
        _stockSearchQuery.value = query
    }

    fun addNewProduct(name: String, tamilName: String, unit: UnitType, stock: Double, rate: Double) {
        val newItem = InventoryItem(
            id = "item_${System.currentTimeMillis()}",
            name = name,
            tamilName = tamilName,
            unit = unit,
            currentStock = stock,
            unitRate = rate
        )
        _inventory.value = _inventory.value + newItem
        _feedbackMessage.value = "$name added to inventory!"
    }

    fun updateStock(itemId: String, newStock: Double, newRate: Double) {
        _inventory.value = _inventory.value.map {
            if (it.id == itemId) it.copy(currentStock = newStock, unitRate = newRate) else it
        }
        _feedbackMessage.value = "Stock updated successfully!"
    }

    // Customer Balance actions
    fun addNewCustomer(name: String, phone: String, initialCredit: Double, address: String) {
        val newCust = Customer(
            id = "cust_${System.currentTimeMillis()}",
            name = name,
            phone = phone,
            pendingBalance = initialCredit,
            address = address
        )
        _customers.value = _customers.value + newCust
        _feedbackMessage.value = "Customer $name added!"
    }

    fun recordPaymentReceived(customerId: String, amount: Double) {
        _customers.value = _customers.value.map {
            if (it.id == customerId) {
                val newBal = (it.pendingBalance - amount).coerceAtLeast(0.0)
                it.copy(pendingBalance = newBal)
            } else it
        }
        _feedbackMessage.value = "Payment of ₹${amount.toInt()} recorded!"
    }

    fun viewBill(bill: SaleBill) {
        _selectedBill.value = bill
        navigateTo(AppScreen.SALES_BILL_VIEW)
    }
}
