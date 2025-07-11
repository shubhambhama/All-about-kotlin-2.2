package org.kotlin.context.service

import org.kotlin.context.model.User

interface NotificationService {
    fun sendEmail(user: User, message: String)
    fun sendSms(user: User, message: String)
}