package org.kotlin.context.logger

interface AuditLogger {
    fun logAction(action: String, userId: String, details: String)
}