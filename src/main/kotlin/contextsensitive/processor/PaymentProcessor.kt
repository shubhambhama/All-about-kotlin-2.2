package org.kotlin.contextsensitive.processor

import org.kotlin.contextsensitive.constants.PaymentStatus
import org.kotlin.contextsensitive.model.PaymentInfo
import org.kotlin.contextsensitive.model.Result

class PaymentProcessor {

    fun processPayment(payment: PaymentInfo): Result<String> {
        return when (payment.status) {
            PaymentStatus.PENDING -> Result.Loading
            PaymentStatus.PROCESSING -> Result.Loading
            PaymentStatus.COMPLETED -> Result.Success("Payment of $${payment.amount} completed successfully")
            PaymentStatus.FAILED -> Result.Error("Payment failed for order ${payment.orderId}")
            PaymentStatus.REFUNDED -> Result.Success("Refund of $${payment.amount} processed")
        }
    }

    fun getPaymentMethodFee(method: String, status: PaymentStatus): Double {
        val baseFee = when (method) {
            "credit_card" -> 2.9
            "debit_card" -> 1.5
            "paypal" -> 3.5
            "bank_transfer" -> 0.0
            else -> 2.0
        }

        return when (status) {
            PaymentStatus.PENDING -> baseFee
            PaymentStatus.PROCESSING -> baseFee
            PaymentStatus.COMPLETED -> baseFee
            PaymentStatus.FAILED -> baseFee + 1.0
            PaymentStatus.REFUNDED -> baseFee + 0.5
        }
    }
}