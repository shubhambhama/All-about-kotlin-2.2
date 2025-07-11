package org.kotlin.context.logger

class ConsoleLogger: Logger {
    override fun info(message: String) = println("INFO: $message")

    override fun debug(message: String) = println("DEBUG: $message")

    override fun error(message: String) = println("ERROR: $message")

    override fun warn(message: String) = println("WARN: $message")
}