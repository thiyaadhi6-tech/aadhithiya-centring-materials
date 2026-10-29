package com.example.data

import com.example.model.BillItem
import com.example.model.Customer
import com.example.model.DailyStat
import com.example.model.Expense
import com.example.model.InventoryItem
import com.example.model.PurchaseEntry
import com.example.model.SaleBill
import com.example.model.UnitType

object SampleData {
    val initialInventory = listOf(
        InventoryItem("item_1", "Adjustable Jack", "அட்ஜஸ்டபிள் ஜாக்", UnitType.NOS, 150.0, 250.0),
        InventoryItem("item_2", "Jockey", "ஜாக்கி", UnitType.NOS, 320.0, 150.0),
        InventoryItem("item_3", "Pipe", "பைப்", UnitType.FEET, 1200.0, 120.0),
        InventoryItem("item_4", "Clamp", "கிளாம்ப்", UnitType.NOS, 450.0, 80.0),
        InventoryItem("item_5", "C-Channel", "சி-சேனல்", UnitType.FEET, 300.0, 180.0),
        InventoryItem("item_6", "Wooden Plate", "மர பலகை", UnitType.NOS, 200.0, 90.0),
        InventoryItem("item_7", "Centring Sheet (Span)", "சென்ட்ரிங் தகடு", UnitType.NOS, 180.0, 320.0),
        InventoryItem("item_8", "Prop Base Plate", "பேஸ் பிளேட்", UnitType.NOS, 500.0, 65.0),
        InventoryItem("item_9", "Tie Rod & Wing Nut", "டை ராட் நட்", UnitType.KG, 150.0, 140.0),
        InventoryItem("item_10", "MS Binding Wire", "பைண்டிங் கம்பி", UnitType.KG, 80.0, 95.0)
    )

    val initialCustomers = listOf(
        Customer("cust_1", "Ramesh Builders", "9842112345", 4500.0, "KK Nagar, Madurai"),
        Customer("cust_2", "Kumar & Co", "9443298765", 2000.0, "Villapuram, Madurai"),
        Customer("cust_3", "Sundar Fabricators", "9789012345", 1500.0, "Simmakkal, Madurai"),
        Customer("cust_4", "Velmurugan", "9894054321", 0.0, "Goripalayam, Madurai"),
        Customer("cust_5", "Selvi Traders", "9944123456", 0.0, "Tallakulam, Madurai"),
        Customer("cust_6", "Meenakshi Construction", "9842567890", 3200.0, "Anna Nagar, Madurai")
    )

    val sampleBill00154 = SaleBill(
        id = "bill_154",
        billNo = "00154",
        customerName = "Walk-in Customer",
        customerPhone = "9952184768",
        date = "24-10-2026",
        time = "02:35 PM",
        items = listOf(
            BillItem("bi_1", "item_1", "Adjustable Jack", "Nos", 10.0, 250.0, 2500.0),
            BillItem("bi_2", "item_2", "Jockey", "Nos", 20.0, 150.0, 3000.0),
            BillItem("bi_3", "item_3", "Pipe", "Feet", 50.0, 120.0, 6000.0),
            BillItem("bi_4", "item_4", "Clamp", "Nos", 10.0, 80.0, 800.0)
        ),
        totalAmount = 12300.0,
        paymentMode = "UPI"
    )

    val initialSalesBills = listOf(
        sampleBill00154,
        SaleBill(
            id = "bill_153",
            billNo = "00153",
            customerName = "Ramesh Builders",
            date = "24-10-2026",
            time = "11:15 AM",
            items = listOf(
                BillItem("bi_5", "item_3", "Pipe", "Feet", 30.0, 120.0, 3600.0),
                BillItem("bi_6", "item_4", "Clamp", "Nos", 12.0, 80.0, 960.0)
            ),
            totalAmount = 4560.0,
            paymentMode = "Cash"
        ),
        SaleBill(
            id = "bill_152",
            billNo = "00152",
            customerName = "Kumar & Co",
            date = "24-10-2026",
            time = "09:40 AM",
            items = listOf(
                BillItem("bi_7", "item_1", "Adjustable Jack", "Nos", 8.0, 250.0, 2000.0),
                BillItem("bi_8", "item_6", "Wooden Plate", "Nos", 20.0, 90.0, 1800.0)
            ),
            totalAmount = 3800.0,
            paymentMode = "Credit"
        ),
        SaleBill(
            id = "bill_151",
            billNo = "00151",
            customerName = "Sundar Fabricators",
            date = "24-10-2026",
            time = "08:30 AM",
            items = listOf(
                BillItem("bi_9", "item_2", "Jockey", "Nos", 15.0, 150.0, 2250.0),
                BillItem("bi_10", "item_4", "Clamp", "Nos", 32.0, 80.0, 2560.0)
            ),
            totalAmount = 4810.0,
            paymentMode = "Cash"
        )
    )

    val samplePurchase = PurchaseEntry(
        id = "pur_1",
        supplierName = "ABC Steel Traders",
        date = "24-10-2026",
        items = listOf(
            BillItem("pi_1", "item_3", "Pipe", "Feet", 500.0, 110.0, 55000.0),
            BillItem("pi_2", "item_4", "Clamp", "Nos", 100.0, 70.0, 7000.0),
            BillItem("pi_3", "item_2", "Jockey", "Nos", 200.0, 130.0, 26000.0)
        ),
        totalAmount = 88000.0,
        paymentType = "Cash"
    )

    val initialPurchases = listOf(
        samplePurchase,
        PurchaseEntry(
            id = "pur_2",
            supplierName = "Southern Scaffolding Supply",
            date = "24-10-2026",
            items = listOf(
                BillItem("pi_4", "item_1", "Adjustable Jack", "Nos", 50.0, 210.0, 10500.0),
                BillItem("pi_5", "item_8", "Prop Base Plate", "Nos", 40.0, 52.5, 2100.0)
            ),
            totalAmount = 12600.0,
            paymentType = "Bank"
        )
    )

    val initialExpenses = listOf(
        Expense("exp_1", "Shop Electricity", 650.0, "24-10-2026", "Utilities"),
        Expense("exp_2", "Delivery Auto Freight", 350.0, "24-10-2026", "Transport")
    )

    val initialMonthStats: List<DailyStat> = (1..31).map { day ->
        val sales = when (day) {
            1 -> 24000.0
            5 -> 32000.0
            10 -> 28000.0
            15 -> 35000.0
            20 -> 22000.0
            24 -> 25480.0
            25 -> 29000.0
            31 -> 31000.0
            else -> (15000 + (day * 670 % 18000)).toDouble()
        }
        val purchase = when (day) {
            1 -> 18000.0
            5 -> 20000.0
            10 -> 15000.0
            15 -> 22000.0
            20 -> 12000.0
            24 -> 12600.0
            25 -> 18000.0
            31 -> 14000.0
            else -> (8000 + (day * 430 % 14000)).toDouble()
        }
        DailyStat(day = day, sales = sales, purchase = purchase)
    }
}
