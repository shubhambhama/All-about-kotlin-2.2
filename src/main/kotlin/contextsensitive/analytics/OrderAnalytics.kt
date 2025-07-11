package org.kotlin.contextsensitive.analytics

import org.kotlin.contextsensitive.constants.OrderStatus
import org.kotlin.contextsensitive.constants.Priority
import org.kotlin.contextsensitive.model.Order

class OrderAnalytics {

    fun analyzeOrders(orders: List<Order>): Map<String, Any> {
        val statusCounts = orders.groupingBy { it.status }.eachCount()
        val paymentStatusCounts = orders.groupingBy { it.paymentStatus }.eachCount()
        val priorityCounts = orders.groupingBy { it.priority }.eachCount()
        val categoryCounts = orders.groupingBy { it.category }.eachCount()

        return mapOf(
            "total_orders" to orders.size,
            "status_breakdown" to statusCounts,
            "payment_breakdown" to paymentStatusCounts,
            "priority_breakdown" to priorityCounts,
            "category_breakdown" to categoryCounts,
            "average_value" to orders.map { it.total }.average(),
            "high_priority_orders" to orders.count { it.priority == Priority.HIGH || it.priority == Priority.CRITICAL }
        )
    }

    fun getRecommendations(analysis: Map<String, Any>): List<String> {
        val recommendations = mutableListOf<String>()

        @Suppress("UNCHECKED_CAST")
        val statusBreakdown = analysis["status_breakdown"] as Map<OrderStatus, Int>

        when {
            statusBreakdown.getOrDefault(OrderStatus.PENDING, 0) > 10 ->
                recommendations.add("High number of pending orders - consider increasing processing capacity")

            statusBreakdown.getOrDefault(OrderStatus.CANCELLED, 0) > 5 ->
                recommendations.add("High cancellation rate - investigate common cancellation reasons")

            statusBreakdown.getOrDefault(OrderStatus.DELIVERED, 0) < statusBreakdown.getOrDefault(OrderStatus.SHIPPED, 0) ->
                recommendations.add("Delivery issues detected - check shipping partners")
        }

        return recommendations
    }
}