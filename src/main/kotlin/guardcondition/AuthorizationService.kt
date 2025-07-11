package org.kotlin.guardcondition

class AuthorizationService {

    private fun getCurrentUserId(): String = "current_user_123"

    fun authorizeRequest(request: ApiRequest): String {
        return when (request) {
            is ApiRequest.GetUser if request.authLevel == AuthLevel.GUEST ->
                "❌ Guests cannot access user data"

            is ApiRequest.GetUser if request.authLevel == AuthLevel.USER && request.userId != getCurrentUserId() ->
                "❌ Users can only access their own data"

            is ApiRequest.GetUser ->
                "✅ Authorized to get user ${request.userId}"

            is ApiRequest.UpdateUser if request.authLevel == AuthLevel.GUEST ->
                "❌ Guests cannot update user data"

            is ApiRequest.UpdateUser if request.authLevel == AuthLevel.USER && request.userId != getCurrentUserId() ->
                "❌ Users can only update their own data"

            is ApiRequest.UpdateUser ->
                "✅ Authorized to update user ${request.userId}"

            is ApiRequest.DeleteUser if request.authLevel != AuthLevel.ADMIN && request.authLevel != AuthLevel.SUPER_ADMIN ->
                "❌ Only admins can delete users"

            is ApiRequest.DeleteUser if request.authLevel == AuthLevel.ADMIN && request.userId == "admin" ->
                "❌ Admins cannot delete admin users"

            is ApiRequest.DeleteUser ->
                "✅ Authorized to delete user ${request.userId}"

            is ApiRequest.GetAllUsers if request.authLevel == AuthLevel.GUEST ->
                "❌ Guests cannot access user list"

            is ApiRequest.GetAllUsers if request.authLevel == AuthLevel.USER ->
                "❌ Regular users cannot access user list"

            is ApiRequest.GetAllUsers ->
                "✅ Authorized to get all users"
        }
    }
}