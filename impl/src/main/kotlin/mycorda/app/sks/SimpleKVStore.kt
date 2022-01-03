package mycorda.app.sks

import mycorda.app.rss.JsonSerialiser


class SimpleKVStore(initialCapacity: Int = 10) : SKS {
    private val lookup = HashMap<Key, SKSValue>(initialCapacity)
    private val rss = JsonSerialiser()

    override fun get(key: Key): SKSValue {
        return lookup[key]!!
    }

    override fun <T> getDeserialised(key: Key): T {
        val raw = get(key)
        if (raw.type == SKSValueType.Serialisable) {
            @Suppress("UNCHECKED_CAST")
            return rss.deserialiseData(raw.value as String).any() as T
        } else {
            throw SKSReadException("${raw.type} is not of type SKSValueType.Serialisable")
        }
    }

    override fun put(key: Key, value: Any, type: SKSValueType): SKSWriter {
        when (type) {
            SKSValueType.Text -> {
                if ((value is String)) {
                    val sksValue = SKSValue(value, type)
                    lookup[key] = sksValue
                } else
                    throw SKSWriteException("value is not a valid SKSValueType.Text type")
            }
            SKSValueType.Binary -> {
                if ((value is ByteArray)) {
                    val sksValue = SKSValue(value, type)
                    lookup[key] = sksValue
                } else
                    throw SKSWriteException("value is not a valid SKSValueType.Binary type")
            }
            SKSValueType.Serialisable -> {
                val serialised = rss.serialiseData(value)
                val sksValue = SKSValue(serialised, type)
                lookup[key] = sksValue
            }
        }

        return this
    }

    override fun remove(key: Key): SKSWriter {
        lookup.remove(key)
        return this
    }

    private fun assert(msg: String, block: () -> Boolean) {
        if (!block.invoke()) throw SKSWriteException(msg)
    }

}
