package org.kotlin.contextsensitive.processor

import org.kotlin.contextsensitive.constants.Category
import org.kotlin.contextsensitive.constants.OrderStatus
import org.kotlin.contextsensitive.constants.PaymentStatus
import org.kotlin.contextsensitive.constants.Priority
import org.kotlin.contextsensitive.model.Order

class OrderProcessor {

    fun processOrder(order: Order): String {

        val statusMessage = when (order.status) {
            OrderStatus.PENDING -> "Order received and pending processing"
            OrderStatus.PROCESSING -> "Order is being processed"
            OrderStatus.SHIPPED -> "Order has been shipped"
            OrderStatus.DELIVERED -> "Order delivered successfully"
            OrderStatus.CANCELLED -> "Order was cancelled"
        }

        val paymentMessage = when (order.paymentStatus) {
            PaymentStatus.PENDING -> "Payment is pending"
            PaymentStatus.PROCESSING -> "Payment is being processed"
            PaymentStatus.COMPLETED -> "Payment completed successfully"
            PaymentStatus.FAILED -> "Payment failed"
            PaymentStatus.REFUNDED -> "Payment refunded"
        }

        val priorityMessage = when (order.priority) {
            Priority.LOW -> "Standard processing time"
            Priority.MEDIUM -> "Expedited processing"
            Priority.HIGH -> "High priority - fast processing"
            Priority.CRITICAL -> "Critical priority - immediate processing"
        }

        return """
            Order ${order.id} Status:
            - Status: $statusMessage
            - Payment: $paymentMessage
            - Priority: $priorityMessage
            - Category: ${order.category}
        """.trimIndent()
    }

    fun getOrderActions(status: OrderStatus): List<String> {
        return when (status) {
            OrderStatus.PENDING -> listOf("Cancel Order", "Modify Order", "Process Order")
            OrderStatus.PROCESSING -> listOf("Cancel Order", "Check Progress", "Expedite Order")
            OrderStatus.SHIPPED -> listOf("Track Package", "Contact Shipping", "Report Issue")
            OrderStatus.DELIVERED -> listOf("Rate Order", "Return Item", "Reorder")
            OrderStatus.CANCELLED -> listOf("View Details", "Reorder", "Contact Support")
        }
    }

    fun calculateShippingCost(category: Category, priority: Priority): Double {
        val baseCost = when (category) {
            Category.ELECTRONICS -> 15.0
            Category.CLOTHING -> 8.0
            Category.BOOKS -> 5.0
            Category.HOME -> 25.0
            Category.SPORTS -> 12.0
        }

        val priorityMultiplier = when (priority) {
            Priority.LOW -> 1.0
            Priority.MEDIUM -> 1.5
            Priority.HIGH -> 2.0
            Priority.CRITICAL -> 3.0
        }

        return baseCost * priorityMultiplier
    }
}