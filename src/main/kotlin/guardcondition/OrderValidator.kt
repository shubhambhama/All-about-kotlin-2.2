package org.kotlin.guardcondition

class OrderValidator {

    data class Order(
        val id: String,
        val customerId: String,
        val amount: Double,
        val items: List<String>,
        val isPriority: Boolean = false,
        val customerTier: String = "standard"
    )

    fun validateOrder(order: Order): String {
        return when {
            order.amount > 10000 && order.customerTier != "premium" ->
                "❌ Large orders require premium membership"

            order.amount > 5000 && order.items.size > 20 ->
                "❌ Large orders with many items require manual review"

            order.amount < 10 && !order.isPriority ->
                "❌ Small orders must be priority orders"

            order.items.isEmpty() ->
                "❌ Order must contain at least one item"

            order.customerId.isBlank() ->
                "❌ Customer ID is required"

            order.amount <= 0 ->
                "❌ Order amount must be positive"

            order.isPriority && order.customerTier == "premium" ->
                "✅ Premium priority order validated"

            order.isPriority ->
                "✅ Priority order validated"

            else ->
                "✅ Standard order validated"
        }
    }
}