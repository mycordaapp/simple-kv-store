package mycorda.app.sks

import mycorda.app.types.Key
import java.lang.RuntimeException

enum class SKSValueType {
    Serialisable,
    Text,
    Binary
}

data class SKSValue(val data: Any, val type: SKSValueType)
data class SKSKeyValue(val key: Key, val value: Any, val type: SKSValueType) {
    fun toText(): String {
        if (type == SKSValueType.Text) {
            return value as String
        } else throw SKSReadException("${type} for ${key} is not of type SKSValueType.Text")
    }

    fun toBinary(): ByteArray {
        if (type == SKSValueType.Binary) {
            return value as ByteArray
        } else throw SKSReadException("${type} for ${key} is not of type SKSValueType.Binary")
    }

}

sealed class SKSException(message: String) : RuntimeException(message)
class SKSWriteException(message: String) : SKSException(message)
class SKSReadException(message: String) : SKSException(message)

interface SKSWriter {
    fun put(kv: SKSKeyValue): SKSWriter

    fun put(key: Key, value: Any, type: SKSValueType): SKSWriter {
        return put(SKSKeyValue(key, value, type))
    }

    fun put(key: Key, value: SKSValue): SKSWriter {
        return put(SKSKeyValue(key, value.data, value.type))
    }

    fun remove(key: Key): SKSWriter
}

interface SKSReader {
    fun get(key: Key): SKSKeyValue

    fun <T> deserialise(kv: SKSKeyValue): T

    fun getList(prefix: Key): Iterable<SKSKeyValue>
}

interface SKS : SKSReader, SKSWriter

