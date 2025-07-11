package org.kotlin.guardcondition

class NetworkManager {

    fun handleResponse(response: NetworkResponse): String {
        return when (response) {
            is NetworkResponse.Success if response.statusCode == 200 ->
                "✅ Success: ${response.data}"

            is NetworkResponse.Success if response.statusCode == 201 ->
                "✅ Created: ${response.data}"

            is NetworkResponse.Success if response.statusCode == 202 ->
                "✅ Accepted: ${response.data}"

            is NetworkResponse.Success ->
                "✅ Success (${response.statusCode}): ${response.data}"

            is NetworkResponse.Error if response.statusCode == 400 ->
                "❌ Bad Request: ${response.message}"

            is NetworkResponse.Error if response.statusCode == 401 ->
                "🔒 Unauthorized: ${response.message}"

            is NetworkResponse.Error if response.statusCode == 403 ->
                "🚫 Forbidden: ${response.message}"

            is NetworkResponse.Error if response.statusCode == 404 ->
                "🔍 Not Found: ${response.message}"

            is NetworkResponse.Error if response.statusCode == 429 ->
                "⏰ Rate Limited: ${response.message}"

            is NetworkResponse.Error if response.statusCode in 500..599 ->
                "🔥 Server Error (${response.statusCode}): ${response.message}"

            is NetworkResponse.Error ->
                "❌ Error (${response.statusCode}): ${response.message}"

            NetworkResponse.Loading ->
                "⏳ Loading..."

            NetworkResponse.Timeout ->
                "⏱️ Request timed out"
        }
    }

    fun simulateRequests(): List<NetworkResponse> {
        return listOf(
            NetworkResponse.Success("User profile data", 200),
            NetworkResponse.Success("Resource created", 201),
            NetworkResponse.Error("Missing required fields", 400),
            NetworkResponse.Error("Invalid credentials", 401),
            NetworkResponse.Error("Access denied", 403),
            NetworkResponse.Error("User not found", 404),
            NetworkResponse.Error("Too many requests", 429),
            NetworkResponse.Error("Internal server error", 500),
            NetworkResponse.Loading,
            NetworkResponse.Timeout
        )
    }
}