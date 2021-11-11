package mycorda.app.sks

import mycorda.app.types.UniqueId
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
    fun put(key: UniqueId, value: Any, type: SKSValueType): SKSWriter
    fun put(key: UniqueId, value: SKSValue): SKSWriter {
        return put(key, value.value, value.type)
    }

    fun remove(key: UniqueId): SKSWriter
}

interface SKSReader {
    fun get(key: UniqueId): SKSValue
    fun getText(key: UniqueId): String {
        val result = get(key)
        if (result.type == SKSValueType.Text) {
            return result.value as String
        } else throw SKSReadException("${result.type} is not of type SKSValueType.Text")
    }

    fun getBinary(key: UniqueId): ByteArray {
        val result = get(key)
        if (result.type == SKSValueType.Binary) {
            return result.value as ByteArray
        } else throw SKSReadException("${result.type} is not of type SKSValueType.Binary")
    }

    fun <T> getDeserialised(key: UniqueId): T

}

interface SKS : SKSReader, SKSWriter

