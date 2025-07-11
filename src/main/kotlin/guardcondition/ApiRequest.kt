package org.kotlin.guardcondition

sealed class ApiRequest {
    data class GetUser(val userId: String, val authLevel: AuthLevel) : ApiRequest()
    data class UpdateUser(val userId: String, val data: String, val authLevel: AuthLevel) : ApiRequest()
    data class DeleteUser(val userId: String, val authLevel: AuthLevel) : ApiRequest()
    data class GetAllUsers(val authLevel: AuthLevel) : ApiRequest()
}