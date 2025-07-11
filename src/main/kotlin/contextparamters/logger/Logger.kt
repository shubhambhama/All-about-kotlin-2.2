package org.kotlin.context.logger

interface Logger {
    fun info(message: String)
    fun debug(message: String)
    fun error(message: String)
    fun warn(message: String)
}