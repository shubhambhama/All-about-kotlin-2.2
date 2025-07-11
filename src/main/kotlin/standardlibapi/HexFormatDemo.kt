package org.kotlin.standardlibapi

import kotlin.text.HexFormat

class HexFormatDemo {

    fun demonstrateBasicHexFormatting() {
        println("=== Basic Hex Formatting ===")

        val numbers = listOf(0, 15, 255, 4095, 65535)

        numbers.forEach { number ->
            val hex = number.toHexString()
            val upperHex = number.toHexString(HexFormat.UpperCase)
            println("$number -> $hex (lowercase), $upperHex (uppercase)")
        }

        val byte: Byte = 255.toByte()
        val short: Short = 4095.toShort()
        val int: Int = 1048575
        val long: Long = 1099511627775L

        println("\nDifferent types:")
        println("Byte ${byte}: ${byte.toHexString()}")
        println("Short ${short}: ${short.toHexString()}")
        println("Int ${int}: ${int.toHexString()}")
        println("Long ${long}: ${long.toHexString()}")
        println()
    }

    fun demonstrateMacAddressFormatting() {
        println("=== MAC Address Formatting ===")

        val macBytes = byteArrayOf(0x00, 0x1b, 0x63, 0x84.toByte(), 0x45, 0xe6.toByte())

        val macFormats = listOf(
            HexFormat {
                upperCase = false
                bytes.byteSeparator = ":"
            },
            HexFormat {
                upperCase = true
                bytes.byteSeparator = "-"
            },
            HexFormat {
                upperCase = false
                bytes.bytesPerGroup = 2
                bytes.groupSeparator = "."
                bytes.byteSeparator = ""
            },
            HexFormat {
                upperCase = true
                bytes.byteSeparator = "-"
            }
        )

        macFormats.forEachIndexed { index, format ->
            val formatName = when (index) {
                0 -> "Standard"
                1 -> "Uppercase with dashes"
                2 -> "Cisco"
                3 -> "Windows"
                else -> "Format $index"
            }
            println("$formatName: ${macBytes.toHexString(format)}")
        }
        println()
    }

    fun demonstrateColorFormatting() {
        println("=== Color Code Formatting ===")

        val colors = listOf(
            Pair("Red", byteArrayOf(255.toByte(), 0, 0)),
            Pair("Green", byteArrayOf(0, 255.toByte(), 0)),
            Pair("Blue", byteArrayOf(0, 0, 255.toByte())),
            Pair("Purple", byteArrayOf(128.toByte(), 0, 128.toByte())),
            Pair("Orange", byteArrayOf(255.toByte(), 165.toByte(), 0))
        )

        val colorFormats = listOf(
            HexFormat {
                upperCase = true
                bytes.byteSeparator = ""
            },
            HexFormat {
                upperCase = true
                bytes.byteSeparator = ""
                number.prefix = "0x"
            },
            HexFormat {
                upperCase = true
                bytes.byteSeparator = "-"
            }
        )

        colors.forEach { (name, rgb) ->
            println("$name:")
            colorFormats.forEachIndexed { index, format ->
                val formatName = when(index) {
                    0 -> "CSS"
                    1 -> "0x prefix"
                    2 -> "Separated"
                    else -> "Format $index"
                }
                val colorCode = rgb.toHexString(format)
                val finalColor = if (index == 0) "#$colorCode" else colorCode
                println("  $formatName: $finalColor")
            }
        }
        println()
    }

    fun demonstrateHexParsing() {
        println("=== Hex Parsing ===")

        val hexStrings = listOf("ff", "FF", "0xFF", "#FF", "255")

        hexStrings.forEach { hexString ->
            try {
                val cleanHex = hexString.removePrefix("#").removePrefix("0x")
                if (cleanHex.all { it.isDigit() || it.lowercaseChar() in 'a'..'f' }) {
                    val number = cleanHex.toInt(16)
                    println("'$hexString' -> $number")
                } else {
                    println("'$hexString' -> Not a valid hex string")
                }
            } catch (e: Exception) {
                println("'$hexString' -> Error: ${e.message}")
            }
        }

        val hexByteStrings = listOf(
            "deadbeef",
            "DE:AD:BE:EF",
            "DE-AD-BE-EF",
            "DEAD.BEEF"
        )

        println("\nParsing byte arrays:")
        hexByteStrings.forEach { hexString ->
            try {
                val cleanHex = hexString.replace(Regex("[^0-9A-Fa-f]"), "")
                val bytes = cleanHex.chunked(2).map { it.toInt(16).toByte() }.toByteArray()
                println("'$hexString' -> ${bytes.contentToString()}")
            } catch (e: Exception) {
                println("'$hexString' -> Error: ${e.message}")
            }
        }
        println()
    }

    fun demonstrateAdvancedFormatting() {
        println("=== Advanced Formatting Examples ===")

        val data = "Hello, World! 🌍".encodeToByteArray()
        val memoryDumpFormat = HexFormat {
            upperCase = true
            bytes {
                bytesPerGroup = 4
                groupSeparator = " "
                byteSeparator = " "
            }
        }

        println("Memory dump style:")
        println(data.toHexString(memoryDumpFormat))
        println("ASCII: ${data.decodeToString()}")

        val uuidBytes = ByteArray(16) { it.toByte() }
        val uuidFormat = HexFormat {
            upperCase = false
            bytes.byteSeparator = ""
        }

        val uuidString = uuidBytes.toHexString(uuidFormat)
        val formattedUuid = "${uuidString.substring(0, 8)}-${uuidString.substring(8, 12)}-${uuidString.substring(12, 16)}-${uuidString.substring(16, 20)}-${uuidString.substring(20)}"
        println("\nUUID-like format: $formattedUuid")

        val instructions = listOf(0x48, 0x89, 0xe5, 0x83, 0xec, 0x10)
        val asmFormat = HexFormat {
            upperCase = false
            number.prefix = "0x"
        }

        println("\nAssembly-style:")
        instructions.forEach { instruction ->
            println("${instruction.toHexString(asmFormat).padStart(6)} ; instruction")
        }
        println()
    }
}