package org.kotlin.context.logger

class DatabaseAuditLogger: AuditLogger {
    override fun logAction(action: String, userId: String, details: String) {
        println("AUDIT: $action - User: $userId - Details: $details")
    }
}