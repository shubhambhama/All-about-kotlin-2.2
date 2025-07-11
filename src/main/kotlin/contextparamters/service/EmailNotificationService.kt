package org.kotlin.context.service

import org.kotlin.context.model.User

class EmailNotificationService: NotificationService {
    override fun sendEmail(user: User, message: String) {
        println("Email sent to ${user.email}: $message")
    }

    override fun sendSms(user: User, message: String) {
        println("SMS sent to ${user.email}: $message")
    }
}