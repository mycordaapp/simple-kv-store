package mycorda.app.sks

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.lang.RuntimeException

class KeyTests {

    @Test
    fun `should throw exception if too long`() {
        assertDoesNotThrow { Key.fromString("a".repeat(255)) }
        assertThrows<RuntimeException> { Key.fromString("a".repeat(256)) }
    }

    @Test
    fun `should throw exception if empty`() {
        assertThrows<RuntimeException> { Key.fromString("") }
        assertDoesNotThrow { Key.fromString("a") }
    }

    @Test
    fun `should allow all supported characters`() {
        assertDoesNotThrow { Key.fromString("abcdefghijklmnopqrstuvwyyz") }
        assertDoesNotThrow { Key.fromString("abcdefghijklmnopqrstuvwyyz".toUpperCase()) }
        assertDoesNotThrow { Key.fromString("1234567890") }
        assertDoesNotThrow { Key.fromString("-_:/") }
    }

    @Test
    fun `should fail for unsupported characters`() {
        // TODO - a better way of generating sets of invalid characters
        val unsupported = "!@£$%^&*()+=;|\\?><.,Ö"
        unsupported.toCharArray().forEach {
            assertThrows<RuntimeException> { Key.fromString(it.toString()) }
        }
    }
}