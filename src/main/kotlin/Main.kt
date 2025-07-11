package org.kotlin

import org.kotlin.context.logger.ConsoleLogger
import org.kotlin.context.logger.DatabaseAuditLogger
import contextparamters.presentation.UserManagementFacade
import org.kotlin.context.repository.InMemoryUserRepository
import org.kotlin.context.service.EmailNotificationService
import org.kotlin.context.service.UserService
import org.kotlin.contextsensitive.analytics.OrderAnalytics
import org.kotlin.contextsensitive.constants.Category
import org.kotlin.contextsensitive.constants.OrderStatus
import org.kotlin.contextsensitive.constants.PaymentStatus
import org.kotlin.contextsensitive.constants.Priority
import org.kotlin.contextsensitive.model.Order
import org.kotlin.contextsensitive.model.PaymentInfo
import org.kotlin.contextsensitive.processor.OrderProcessor
import org.kotlin.contextsensitive.processor.PaymentProcessor
import org.kotlin.guardcondition.ApiRequest
import org.kotlin.guardcondition.AuthLevel
import org.kotlin.guardcondition.AuthorizationService
import org.kotlin.guardcondition.DatabaseManager
import org.kotlin.guardcondition.DatabaseQuery
import org.kotlin.guardcondition.FileManager
import org.kotlin.guardcondition.FileOperation
import org.kotlin.guardcondition.NetworkManager
import org.kotlin.guardcondition.OrderValidator
import org.kotlin.nestedtypealiases.ApiClient
import org.kotlin.nestedtypealiases.ConfigurationManager
import org.kotlin.nestedtypealiases.EventSystem
import org.kotlin.nestedtypealiases.GameEngine
import org.kotlin.standardlibapi.Base64Demo
import org.kotlin.standardlibapi.HexFormatDemo

fun main() {
//    letsCallContextParameters()
//    letsCallContextSensitiveResolution()
//    demonstrateGuardConditions()
//    letsCallBase64API()
//    letsCallHexFormatAPI()
    letsCallNestedTypeAliases()
}

fun letsCallContextParameters() {
    println("=== Context-Parameter Demo ===")

    val logger = ConsoleLogger()
    val userRepo = InMemoryUserRepository()
    val notificationService = EmailNotificationService()
    val auditLogger = DatabaseAuditLogger()

    with(logger) {
        with(userRepo) {
            with(notificationService) {
                with(auditLogger) {
                    val userService = UserService()
                    val facade = UserManagementFacade()

                    println("\n--- Creating Users ---")
                    val result1 = facade.processUserRegistration("Alice Cooper", "alice@example.com")
                    println("Result: $result1")

                    val result2 = facade.processUserRegistration("Charlie Brown", "charlie@example.com")
                    println("Result: $result2")

                    println("\n--- Duplicate User Test ---")
                    val duplicateResult = facade.processUserRegistration("Alice Cooper", "alice@example.com")
                    println("Result: $duplicateResult")

                    println("\n--- Updating User ---")
                    val updatedUser = userService.updateUser("1", "John Updated", "john.updated@example.com")
                    println("Updated user: $updatedUser")

                    println("\n--- Deactivating User ---")
                    val deactivated = userService.deactivateUser("2")
                    println("Deactivation successful: $deactivated")

                    println("\n--- User Statistics ---")
                    val stats = userService.getUserStats()
                    stats.forEach { (key, value) ->
                        println("$key: $value")
                    }
                }
            }
        }
    }
}

fun letsCallContextSensitiveResolution() {
    println("=== Context-Sensitive Resolution Demo ===")

    val orderProcessor = OrderProcessor()
    val paymentProcessor = PaymentProcessor()
    val analytics = OrderAnalytics()

    val orders = listOf(
        Order(
            id = "ORD-001",
            customerId = "CUST-001",
            items = listOf("Laptop", "Mouse"),
            total = 1299.99,
            status = OrderStatus.PROCESSING,
            paymentStatus = PaymentStatus.COMPLETED,
            priority = Priority.HIGH,
            category = Category.ELECTRONICS
        ),
        Order(
            id = "ORD-002",
            customerId = "CUST-002",
            items = listOf("T-Shirt", "Jeans"),
            total = 89.99,
            status = OrderStatus.SHIPPED,
            paymentStatus = PaymentStatus.COMPLETED,
            priority = Priority.LOW,
            category = Category.CLOTHING
        ),
        Order(
            id = "ORD-003",
            customerId = "CUST-003",
            items = listOf("Programming Book"),
            total = 45.00,
            status = OrderStatus.PENDING,
            paymentStatus = PaymentStatus.PENDING,
            priority = Priority.MEDIUM,
            category = Category.BOOKS
        )
    )

    println("\n--- Processing Orders ---")
    orders.forEach { order ->
        println(orderProcessor.processOrder(order))
        println("Available actions: ${orderProcessor.getOrderActions(order.status)}")
        println("Shipping cost: $${orderProcessor.calculateShippingCost(order.category, order.priority)}")
        println("---")
    }

    println("\n--- Processing Payments ---")
    orders.forEach { order ->
        val paymentInfo = PaymentInfo(order.id, order.total, order.paymentStatus, "credit_card")
        val result = paymentProcessor.processPayment(paymentInfo)
        val fee = paymentProcessor.getPaymentMethodFee("credit_card", order.paymentStatus)

        println("Order ${order.id}: $result")
        println("Payment fee: $${fee}")
        println("---")
    }

    println("\n--- Order Analytics ---")
    val analysis = analytics.analyzeOrders(orders)
    analysis.forEach { (key, value) ->
        println("$key: $value")
    }

    println("\n--- Recommendations ---")
    val recommendations = analytics.getRecommendations(analysis)
    recommendations.forEach { recommendation ->
        println("• $recommendation")
    }
}

fun demonstrateGuardConditions() {
    println("=== Guard Conditions Demo ===")

    val networkManager = NetworkManager()
    val authService = AuthorizationService()
    val fileManager = FileManager()
    val dbManager = DatabaseManager()
    val orderValidator = OrderValidator()

    println("\n--- Network Response Handling ---")
    networkManager.simulateRequests().forEach { response ->
        println(networkManager.handleResponse(response))
    }

    println("\n--- Authorization Examples ---")
    val authRequests = listOf(
        ApiRequest.GetUser("user123", AuthLevel.USER),
        ApiRequest.GetUser("other_user", AuthLevel.USER),
        ApiRequest.UpdateUser("user123", "new_data", AuthLevel.USER),
        ApiRequest.DeleteUser("user123", AuthLevel.USER),
        ApiRequest.DeleteUser("user123", AuthLevel.ADMIN),
        ApiRequest.GetAllUsers(AuthLevel.GUEST),
        ApiRequest.GetAllUsers(AuthLevel.ADMIN)
    )

    authRequests.forEach { request ->
        println(authService.authorizeRequest(request))
    }

    println("\n--- File Operations ---")
    val fileOps = listOf(
        FileOperation.Read("document.pdf", 50_000),
        FileOperation.Read("huge_file.zip", 150_000_000),
        FileOperation.Read("temp_file.tmp", 1000),
        FileOperation.Write("output.txt", 1000, "Hello World"),
        FileOperation.Write("large_file.dat", 600_000_000, "Data"),
        FileOperation.Write("../../../etc/passwd", 100, "malicious"),
        FileOperation.Delete("system_config.ini", 5000),
        FileOperation.Delete("user_data.json", 10000)
    )

    fileOps.forEach { op ->
        println(fileManager.processFileOperation(op))
    }

    println("\n--- Database Operations ---")
    val queries = listOf(
        DatabaseQuery("users", "SELECT", 500, false, 1),
        DatabaseQuery("users", "SELECT", 1500, false, 1),
        DatabaseQuery("users", "DELETE", 200, false, 1),
        DatabaseQuery("users", "UPDATE", 75, false, 1),
        DatabaseQuery("users", "DROP", 1, false, 1),
        DatabaseQuery("orders", "INSERT", 10, true, 8),
        DatabaseQuery("products", "UPDATE", 25, true, 3)
    )

    queries.forEach { query ->
        println(dbManager.executeQuery(query))
    }

    println("\n--- Order Validation ---")
    val orders = listOf(
        OrderValidator.Order("1", "cust1", 15000.0, listOf("laptop", "mouse"), false, "standard"),
        OrderValidator.Order("2", "cust2", 8000.0, (1..25).map { "item$it" }, false, "premium"),
        OrderValidator.Order("3", "cust3", 5.0, listOf("pen"), false, "standard"),
        OrderValidator.Order("4", "cust4", 2500.0, listOf("phone", "case"), true, "premium"),
        OrderValidator.Order("5", "", 100.0, listOf("book"), false, "standard"),
        OrderValidator.Order("6", "cust6", -50.0, listOf("item"), false, "standard")
    )

    orders.forEach { order ->
        println("Order ${order.id}: ${orderValidator.validateOrder(order)}")
    }
}

fun letsCallBase64API() {
    println("=== Kotlin 2.2.0 Base64 API Demo ===")

    val demo = Base64Demo()

    demo.demonstrateBasicEncoding()
    demo.demonstrateAllEncodingTypes()
    demo.demonstrateCustomPadding()
    demo.demonstrateImageEncoding()
    demo.demonstrateJwtTokenPattern()
    demo.demonstrateEmailAttachment()
    demo.demonstrateErrorHandling()
}

fun letsCallHexFormatAPI() {
    println("=== Kotlin 2.2.0 HexFormat API Demo ===")

    val demo = HexFormatDemo()

    demo.demonstrateBasicHexFormatting()
    demo.demonstrateMacAddressFormatting()
    demo.demonstrateColorFormatting()
    demo.demonstrateHexParsing()
    demo.demonstrateAdvancedFormatting()
}

fun letsCallNestedTypeAliases() {
    println("=== Nested Type Aliases Demo ===")

    println("\n--- Database Manager ---")
    val dbManager = org.kotlin.nestedtypealiases.DatabaseManager()

    dbManager.addConnection("primary", "localhost" to 5432)
    dbManager.addConnection("secondary", "backup-server" to 5433)

    val result = dbManager.executeQuery("primary", "SELECT * FROM users")
    println("Query result: $result")

    dbManager.executeTransaction(
        "primary",
        listOf("INSERT INTO users ...", "UPDATE users SET ...", "DELETE FROM logs ...")
    ) { transactionResult ->
        println("Transaction completed: $transactionResult")
    }

    println("\n--- API Client ---")
    val apiClient = ApiClient()

    apiClient.addInterceptor { url, headers ->
        headers + ("Authorization" to "Bearer token123")
    }

    apiClient.addTransformer("application/json") { (statusCode, headers, body) ->
        if (statusCode == 200) {
            mapOf("parsed" to true, "data" to body)
        } else {
            mapOf("error" to true, "message" to body)
        }
    }

    val response = apiClient.makeRequest("https://api.example.com/users")
    val processed = apiClient.processResponse(response)
    println("API Response: $processed")

    println("\n--- Event System ---")
    val eventSystem = EventSystem()

    eventSystem.subscribe("user.created") { data ->
        println("User created: ${data["username"]}")
    }

    eventSystem.subscribe("user.updated") { data ->
        println("User updated: ${data["username"]} - ${data["changes"]}")
    }

    eventSystem.addFilter("user.created") { data ->
        val username = data["username"] as? String
        username?.isNotEmpty() == true
    }

    eventSystem.emit("user.created", mapOf("username" to "alice", "email" to "alice@example.com"))
    eventSystem.emit("user.created", mapOf("username" to "", "email" to "invalid@example.com"))
    eventSystem.emit("user.updated", mapOf("username" to "alice", "changes" to "email updated"))

    println("\n--- Configuration Manager ---")
    val configManager = ConfigurationManager()

    configManager.setValidator("max_connections") { value ->
        (value as? Int)?.let { it > 0 && it <= 1000 } == true
    }

    configManager.setValidator("server_name") { value ->
        (value as? String)?.isNotEmpty() == true
    }

    configManager.setTransformer("server_name") { value ->
        (value as? String)?.lowercase()
    }

    configManager.set("max_connections", 50)
    configManager.set("server_name", "PRODUCTION-SERVER")
    configManager.set("debug_mode", true)

    println("Server name: ${configManager.get("server_name")}")
    println("Max connections: ${configManager.get("max_connections")}")

    val dbConfig = mapOf(
        "db.host" to "localhost",
        "db.port" to 5432,
        "db.name" to "myapp"
    )

    val loadedCount = configManager.loadSection(dbConfig)
    println("Loaded $loadedCount configuration items")

    println("\n--- Game Engine ---")
    val gameEngine = GameEngine()

    val playerId = gameEngine.createGameObject("player", 100f to 200f)
    val enemyId = gameEngine.createGameObject("enemy", 300f to 150f)

    println("Created game objects: $playerId, $enemyId")

    gameEngine.update(0.016f)

    println("\nNested type aliases provide better organization and encapsulation!")
}