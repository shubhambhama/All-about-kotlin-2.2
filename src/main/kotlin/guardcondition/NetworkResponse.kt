package org.kotlin.guardcondition

sealed class NetworkResponse {
    data class Success(val data: String, val statusCode: Int = 200) : NetworkResponse()
    data class Error(val message: String, val statusCode: Int) : NetworkResponse()
    data object Loading : NetworkResponse()
    data object Timeout : NetworkResponse()
}