package org.kotlin.contextsensitive.model

import org.kotlin.contextsensitive.constants.Category
import org.kotlin.contextsensitive.constants.OrderStatus
import org.kotlin.contextsensitive.constants.PaymentStatus
import org.kotlin.contextsensitive.constants.Priority

data class Order(
    val id: String,
    val customerId: String,
    val items: List<String>,
    val total: Double,
    val status: OrderStatus,
    val paymentStatus: PaymentStatus,
    val priority: Priority,
    val category: Category
)