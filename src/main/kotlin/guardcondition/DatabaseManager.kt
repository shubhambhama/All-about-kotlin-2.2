package org.kotlin.guardcondition

class DatabaseManager {
    fun executeQuery(query: DatabaseQuery): String {
        return when {
            query.table == "users" && query.operation == "SELECT" && !query.isTransaction && query.recordCount > 1000 ->
                "⚠️ Large query detected - consider adding pagination"

            query.table == "users" && query.operation == "DELETE" && !query.isTransaction && query.recordCount > 100 ->
                "❌ Bulk delete requires transaction"

            query.table == "users" && query.operation == "UPDATE" && !query.isTransaction && query.recordCount > 50 ->
                "❌ Bulk update requires transaction"

            query.operation == "DROP" && !query.isTransaction ->
                "❌ DROP operations require transaction"

            query.isTransaction && query.priority > 5 ->
                "🚀 High priority transaction on ${query.table}: ${query.operation} (${query.recordCount} records)"

            query.isTransaction ->
                "✅ Transaction on ${query.table}: ${query.operation} (${query.recordCount} records)"

            else ->
                "✅ Query on ${query.table}: ${query.operation} (${query.recordCount} records)"
        }
    }
}