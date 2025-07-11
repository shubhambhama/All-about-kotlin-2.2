package org.kotlin.standardlibapi

import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class)
class Base64Demo {

    fun demonstrateBasicEncoding() {
        println("=== Basic Base64 Encoding ===")

        val originalText = "Hello, Kotlin 2.2.0! 🚀"
        val originalBytes = originalText.encodeToByteArray()

        val encoded = Base64.Default.encode(originalBytes)
        println("Original: $originalText")
        println("Encoded: $encoded")

        val decoded = Base64.Default.decode(encoded)
        println("Decoded: ${decoded.decodeToString()}")
        println("Match: ${originalText == decoded.decodeToString()}")
        println()
    }

    fun demonstrateAllEncodingTypes() {
        println("=== All Base64 Encoding Types ===")

        val testData = "Testing all Base64 variants: +/=?&"
        val testBytes = testData.encodeToByteArray()

        val defaultEncoded = Base64.Default.encode(testBytes)
        println("Default: $defaultEncoded")

        val urlSafeEncoded = Base64.UrlSafe.encode(testBytes)
        println("URL-Safe: $urlSafeEncoded")

        val mimeEncoded = Base64.Mime.encode(testBytes)
        println("MIME: $mimeEncoded")

        val pemEncoded = Base64.Pem.encode(testBytes)
        println("PEM: $pemEncoded")

        val shortText = "Hi"
        val shortBytes = shortText.encodeToByteArray()

        println("\nPadding differences with short text '$shortText':")
        println("Default: ${Base64.Default.encode(shortBytes)}")
        println("URL-Safe: ${Base64.UrlSafe.encode(shortBytes)}")
        println()
    }

    fun demonstrateCustomPadding() {
        println("=== Custom Padding Options ===")

        val data = "Padding test"
        val bytes = data.encodeToByteArray()

        val withPadding = Base64.Default.encode(bytes)
        println("With padding: $withPadding")

        val withoutPadding = Base64.Default.withPadding(Base64.PaddingOption.ABSENT).encode(bytes)
        println("Without padding: $withoutPadding")

        val presentPadding = Base64.Default.withPadding(Base64.PaddingOption.PRESENT).encode(bytes)
        println("Present padding: $presentPadding")

        val decoded1 = Base64.Default.decode(withPadding)
        val decoded2 = Base64.Default.withPadding(Base64.PaddingOption.ABSENT).decode(withoutPadding)

        println("Both decode correctly: ${decoded1.decodeToString() == decoded2.decodeToString()}")
        println()
    }

    fun demonstrateImageEncoding() {
        println("=== Image-like Binary Data Encoding ===")

        val imageData = ByteArray(100) { it.toByte() }

        val base64Image = Base64.Default.encode(imageData)
        val dataUrl = "data:image/png;base64,${base64Image}"

        println("Simulated image data size: ${imageData.size} bytes")
        println("Base64 encoded size: ${base64Image.length} bytes")
        println("Size increase: ${((base64Image.length.toDouble() / imageData.size - 1) * 100).toInt()}%")
        println("Data URL (first 50 chars): ${dataUrl.take(50)}...")
        println()
    }

    fun demonstrateJwtTokenPattern() {
        println("=== JWT Token Pattern Simulation ===")

        val header = """{"alg":"HS256","typ":"JWT"}"""
        val payload = """{"sub":"user123","name":"Shubham Bhama","iat":1516239022}"""
        val signature = "secret_signature_data"

        val encodedHeader = Base64.UrlSafe.withPadding(Base64.PaddingOption.ABSENT).encode(header.encodeToByteArray())
        val encodedPayload = Base64.UrlSafe.withPadding(Base64.PaddingOption.ABSENT).encode(payload.encodeToByteArray())
        val encodedSignature =
            Base64.UrlSafe.withPadding(Base64.PaddingOption.ABSENT).encode(signature.encodeToByteArray())

        val jwtToken = "$encodedHeader.$encodedPayload.$encodedSignature"

        println("JWT Token Components:")
        println("Header: $header")
        println("Payload: $payload")
        println("Signature: $signature")
        println("\nJWT Token: $jwtToken")

        val decodedHeader =
            Base64.UrlSafe.withPadding(Base64.PaddingOption.ABSENT).decode(encodedHeader.encodeToByteArray())
                .decodeToString()
        println("\nDecoded header: $decodedHeader")
        println("Header matches: ${header == decodedHeader}")
        println()
    }

    fun demonstrateEmailAttachment() {
        println("=== Email Attachment Encoding ===")

        val fileName = "document.txt"
        val fileContent = """
            This is a sample document that would be attached to an email.
            It contains multiple lines of text.
            
            Special characters: @#$%^&*()
            Unicode: 🚀 📧 💾
            
            End of document.
        """.trimIndent()

        val fileBytes = fileContent.encodeToByteArray()

        val mimeEncoded = Base64.Mime.encode(fileBytes)
        val mimeString = mimeEncoded

        println("Email attachment simulation:")
        println("File: $fileName")
        println("Size: ${fileBytes.size} bytes")
        println("MIME Base64 encoded (with line breaks):")
        println(mimeString)

        val decoded = Base64.Mime.decode(mimeEncoded)
        println("\nDecoding successful: ${fileContent == decoded.decodeToString()}")
        println()
    }

    fun demonstrateErrorHandling() {
        println("=== Error Handling ===")

        try {
            val invalidBase64 = "This is not valid Base64!"
            Base64.Default.decode(invalidBase64.encodeToByteArray())
            println("Should not reach here")
        } catch (e: Exception) {
            println("Caught expected error: ${e.message}")
        }

        try {
            val invalidChars = "SGVsbG8h@#$%"
            Base64.Default.decode(invalidChars.encodeToByteArray())
            println("Should not reach here")
        } catch (e: Exception) {
            println("Caught expected error: ${e.message}")
        }
        println()
    }
}