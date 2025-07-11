package org.kotlin.nestedtypealiases

import kotlin.random.Random

class DatabaseManager {
    typealias QueryResult = Map<String, Any?>
    typealias ConnectionConfig = Pair<String, Int>
    typealias TransactionCallback = (QueryResult) -> Unit
    typealias ValidationRule = (Any?) -> Boolean

    private val connections = mutableMapOf<String, ConnectionConfig>()
    private val queryCache = mutableMapOf<String, QueryResult>()

    fun addConnection(name: String, config: ConnectionConfig): Boolean {
        connections[name] = config
        println("Added connection '$name' to ${config.first}:${config.second}")
        return true
    }

    fun executeQuery(connectionName: String, query: String): QueryResult {
        val config = connections[connectionName] ?: return emptyMap()

        val result: QueryResult = mapOf(
            "connection" to "${config.first}:${config.second}",
            "query" to query,
            "rows" to Random.nextInt(1, 100),
            "timestamp" to System.currentTimeMillis()
        )

        queryCache[query] = result
        return result
    }

    fun executeTransaction(
        connectionName: String,
        queries: List<String>,
        callback: TransactionCallback
    ) {
        println("Starting transaction on '$connectionName'")

        val results = queries.map { query ->
            executeQuery(connectionName, query)
        }

        val transactionResult: QueryResult = mapOf(
            "transaction_id" to Random.nextLong(),
            "queries_executed" to results.size,
            "total_rows" to results.sumOf { it["rows"] as? Int ?: 0 }
        )

        callback(transactionResult)
        println("Transaction completed")
    }

    fun validateData(data: Any?, rules: List<ValidationRule>): Boolean {
        return rules.all { rule -> rule(data) }
    }
}