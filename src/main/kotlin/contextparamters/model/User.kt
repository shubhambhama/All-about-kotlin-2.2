package org.kotlin.context.model

data class User(
    val id: String,
    val name: String,
    val email: String,
    val isActive: Boolean = true
)