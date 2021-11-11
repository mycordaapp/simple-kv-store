package mycorda.app.sks

import mycorda.app.rss.JsonSerialiser
import mycorda.app.types.UniqueId


class SimpleKVStore(initialCapacity: Int = 10) : SKS {
    private val lookup = HashMap<UniqueId, SKSValue>(initialCapacity)
    private val rss = JsonSerialiser()

    override fun get(key: UniqueId): SKSValue {
        return lookup[key]!!
    }

    override fun <T> getDeserialised(key: UniqueId): T {
        val raw = get(key)
        if (raw.type == SKSValueType.Serialisable) {
            return rss.deserialiseData(raw.value as String).any() as T
        } else {
            throw SKSReadException("${raw.type} is not of type SKSValueType.Serialisable")
        }
    }

    override fun put(key: UniqueId, value: Any, type: SKSValueType): SKSWriter {
        when (type) {
            SKSValueType.Text -> {
                if ((value is String)) {
                    val value = SKSValue(value, type)
                    lookup[key] = value
                } else
                    throw SKSWriteException("value is not a valid SKSValueType.Text type")
            }
            SKSValueType.Binary -> {
                if ((value is ByteArray)) {
                    val value = SKSValue(value, type)
                    lookup[key] = value
                } else
                    throw SKSWriteException("value is not a valid SKSValueType.Binary type")
            }
            SKSValueType.Serialisable -> {
                val serialised = rss.serialiseData(value)
                val value = SKSValue(serialised, type)
                lookup[key] = value
            }
        }

        return this
    }

    override fun remove(key: UniqueId): SKSWriter {
        lookup.remove(key)
        return this
    }

    private fun assert(msg: String, block: () -> Boolean) {
        if (!block.invoke()) throw SKSWriteException(msg)
    }

}
