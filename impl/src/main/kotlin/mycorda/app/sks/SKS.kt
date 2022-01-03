package mycorda.app.sks

import java.lang.RuntimeException

enum class SKSValueType {
    Serialisable,
    Text,
    Binary
}

data class SKSValue(val value: Any, val type: SKSValueType)

sealed class SKSException(message: String) : RuntimeException(message)
class SKSWriteException(message: String) : SKSException(message)
class SKSReadException(message: String) : SKSException(message)


interface SKSWriter {
    fun put(key: Key, value: Any, type: SKSValueType): SKSWriter
    fun put(key: Key, value: SKSValue): SKSWriter {
        return put(key, value.value, value.type)
    }

    fun remove(key: Key): SKSWriter
}

interface SKSReader {
    fun get(key: Key): SKSValue
    fun getText(key: Key): String {
        val result = get(key)
        if (result.type == SKSValueType.Text) {
            return result.value as String
        } else throw SKSReadException("${result.type} is not of type SKSValueType.Text")
    }

    fun getBinary(key: Key): ByteArray {
        val result = get(key)
        if (result.type == SKSValueType.Binary) {
            return result.value as ByteArray
        } else throw SKSReadException("${result.type} is not of type SKSValueType.Binary")
    }

    fun <T> getDeserialised(key: Key): T

}

interface SKS : SKSReader, SKSWriter

