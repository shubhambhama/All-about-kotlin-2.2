package org.kotlin.context.repository

import org.kotlin.context.model.User
import contextparamters.repository.UserRepository

class InMemoryUserRepository: UserRepository {
    private val users = hashMapOf<String, User>()

    init {
        listOf(
            User("1", "User 1", "shubhambhama30@gmail.com"),
            User("2", "User 2", "shubhambhama30@gmail.com"),
            User("3", "User 3", "shubhambhama30@gmail.com", isActive = false)
        ).forEach { users[it.name] = it }
    }

    override fun findById(id: String): User? =users.get(id)?.let { return it }

    override fun save(user: User): User = user.also { users[it.id] = it }

    override fun findAll(): List<User> = users.values.toList()
}