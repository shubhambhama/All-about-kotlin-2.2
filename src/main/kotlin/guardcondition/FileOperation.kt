package org.kotlin.guardcondition

sealed class FileOperation {
    data class Read(val filename: String, val size: Long) : FileOperation()
    data class Write(val filename: String, val size: Long, val content: String) : FileOperation()
    data class Delete(val filename: String, val size: Long) : FileOperation()
}