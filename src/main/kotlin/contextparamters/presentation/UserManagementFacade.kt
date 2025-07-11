package contextparamters.presentation

import contextparamters.repository.UserRepository
import org.kotlin.context.logger.AuditLogger
import org.kotlin.context.logger.Logger
import org.kotlin.context.model.User
import org.kotlin.context.service.NotificationService
import org.kotlin.context.service.UserService

class UserManagementFacade {

    context(logger: Logger, userRepo: UserRepository, notificationService: NotificationService, auditLogger: AuditLogger)
    fun processUserRegistration(name: String, email: String): String {
        logger.info("Processing user registration for: $email")

        val existingUsers = userRepo.findAll()
        val existingUser = existingUsers.find { it.email == email }

        if (existingUser != null) {
            logger.info("User already exists: $email")
            return "User with email $email already exists"
        }

        val userService = UserService()
        val newUser = userService.createUser(name, email)

        return if (newUser != null) {
            sendWelcomeNotifications(newUser)
            "User registered successfully: ${newUser.id}"
        } else {
            "Failed to register user"
        }
    }

    context(logger: Logger, userRepo: UserRepository, notificationService: NotificationService, auditLogger: AuditLogger)
    private fun sendWelcomeNotifications(user: User) {
        logger.info("Sending welcome notifications to: ${user.email}")

        notificationService.sendEmail(user, "Welcome! Your account is now active.")
        notificationService.sendSms(user, "Welcome to our platform!")

        auditLogger.logAction("WELCOME_SENT", user.id, "Welcome notifications sent")
    }
}