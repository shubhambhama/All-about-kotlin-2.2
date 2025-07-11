package org.kotlin.guardcondition

data class DatabaseQuery(
    val table: String,
    val operation: String,
    val recordCount: Int,
    val isTransaction: Boolean = false,
    val priority: Int = 1
)