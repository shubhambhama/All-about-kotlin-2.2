package org.kotlin.guardcondition

class FileManager {

    fun processFileOperation(operation: FileOperation): String {
        return when (operation) {
            is FileOperation.Read if operation.size > 100_000_000 ->
                "❌ File too large to read (${operation.size} bytes)"

            is FileOperation.Read if operation.filename.endsWith(".tmp") ->
                "❌ Cannot read temporary files"

            is FileOperation.Read ->
                "✅ Reading file: ${operation.filename} (${operation.size} bytes)"

            is FileOperation.Write if operation.size > 500_000_000 ->
                "❌ File too large to write (${operation.size} bytes)"

            is FileOperation.Write if operation.content.isBlank() ->
                "❌ Cannot write empty content"

            is FileOperation.Write if operation.filename.contains("..") ->
                "❌ Invalid filename - contains path traversal"

            is FileOperation.Write ->
                "✅ Writing file: ${operation.filename} (${operation.size} bytes)"

            is FileOperation.Delete if operation.filename.startsWith("system_") ->
                "❌ Cannot delete system files"

            is FileOperation.Delete if operation.size == 0L ->
                "❌ Cannot delete empty files"

            is FileOperation.Delete ->
                "✅ Deleting file: ${operation.filename}"
        }
    }
}