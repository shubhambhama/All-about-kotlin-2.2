package org.kotlin.context.service

import contextparamters.repository.UserRepository
import org.kotlin.context.logger.AuditLogger
import org.kotlin.context.logger.Logger
import org.kotlin.context.model.User
import kotlin.random.Random

class UserService {

    private fun generateUserId(): String = Random.nextInt(1000, 9999).toString()

    context(logger: Logger, userRepo: UserRepository, notificationService: NotificationService, auditLogger: AuditLogger)
    fun createUser(name: String, email: String): User? {
        logger.info("Creating new user: $name")
        auditLogger.logAction("CREATE_USER", "system", "Creating user: $name")

        val userId = generateUserId()
        val user = User(userId, name, email)

        return try {
            val savedUser = userRepo.save(user)
            notificationService.sendEmail(savedUser, "Welcome to our platform!")
            logger.info("User created successfully: ${savedUser.id}")
            savedUser
        } catch (e: Exception) {
            logger.error("Failed to create user: ${e.message}")
            null
        }
    }

    context(logger: Logger, userRepo: UserRepository, notificationService: NotificationService, auditLogger: AuditLogger)
    fun updateUser(userId: String, newName: String?, newEmail: String?): User? {
        logger.info("Updating user: $userId")
        auditLogger.logAction("UPDATE_USER", userId, "Updating user details")

        val user = userRepo.findById(userId) ?: run {
            logger.error("User not found: $userId")
            return null
        }

        val updatedUser = user.copy(
            name = newName ?: user.name,
            email = newEmail ?: user.email
        )

        return userRepo.save(updatedUser).also {
            logger.info("User updated successfully: ${it.id}")
            notificationService.sendEmail(it, "Your profile has been updated")
        }
    }

    context(logger: Logger, userRepo: UserRepository, notificationService: NotificationService, auditLogger: AuditLogger)
    fun deactivateUser(userId: String): Boolean {
        logger.info("Deactivating user: $userId")
        auditLogger.logAction("DEACTIVATE_USER", userId, "User deactivation")

        val user = userRepo.findById(userId) ?: run {
            logger.error("User not found: $userId")
            return false
        }

        if (!user.isActive) {
            logger.info("User already inactive: $userId")
            return true
        }

        val deactivatedUser = user.copy(isActive = false)
        userRepo.save(deactivatedUser)

        notificationService.sendEmail(deactivatedUser, "Your account has been deactivated")
        logger.info("User deactivated successfully: $userId")
        return true
    }

    context(logger: Logger, userRepo: UserRepository, notificationService: NotificationService, auditLogger: AuditLogger)
    fun getUserStats(): Map<String, Int> {
        logger.debug("Generating user statistics")
        auditLogger.logAction("GET_STATS", "system", "Generating user statistics")

        val users = userRepo.findAll()
        return mapOf(
            "total" to users.size,
            "active" to users.count { it.isActive },
            "inactive" to users.count { !it.isActive }
        )
    }
}


class Loggers {
    fun log(message: String) {
        print(message)
    }
}

context(user: User, loggers: Loggers)
fun printUserInfo() {
    loggers.log("User: ${user.name}")
}