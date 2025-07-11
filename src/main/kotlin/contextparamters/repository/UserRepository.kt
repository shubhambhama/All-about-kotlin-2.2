package contextparamters.repository

import org.kotlin.context.model.User

interface UserRepository {
    fun findById(id: String): User?
    fun save(user: User): User
    fun findAll(): List<User>
}