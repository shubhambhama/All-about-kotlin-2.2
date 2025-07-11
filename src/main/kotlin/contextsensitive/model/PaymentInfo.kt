package org.kotlin.contextsensitive.model

import org.kotlin.contextsensitive.constants.PaymentStatus

data class PaymentInfo(
    val orderId: String,
    val amount: Double,
    val status: PaymentStatus,
    val method: String
)