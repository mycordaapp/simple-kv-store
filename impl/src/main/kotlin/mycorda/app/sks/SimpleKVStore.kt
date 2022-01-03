package mycorda.app.sks

import mycorda.app.rss.JsonSerialiser
import mycorda.app.types.Key


class SimpleKVStore(initialCapacity: Int = 10) : SKS {
    private val lookup = HashMap<Key, SKSValue>(initialCapacity)
    private val rss = JsonSerialiser()

    override fun get(key: Key): SKSKeyValue {
        val value = lookup[key]!!
        return SKSKeyValue(key, value.data, value.type)
    }

    override fun <T> deserialise(kv: SKSKeyValue): T {
        if (kv.type == SKSValueType.Serialisable) {
            @Suppress("UNCHECKED_CAST")
            return rss.deserialiseData(kv.value as String).any() as T
        } else {
            throw SKSReadException("${kv.type} is not of type SKSValueType.Serialisable")
        }
    }

    override fun getList(prefix: Key): Iterable<SKSKeyValue> {
        val keys = lookup.keys
            .filter { it.key.startsWith(prefix.key) }
            .sortedBy { it.key }

        return keys.map { get(it) }
    }


    override fun put(kv: SKSKeyValue): SKSWriter {
        when (kv.type) {
            SKSValueType.Text -> {
                if ((kv.value is String)) {
                    val sksValue = SKSValue(kv.value, kv.type)
                    lookup[kv.key] = sksValue
                } else
                    throw SKSWriteException("value is not a valid SKSValueType.Text type")
            }
            SKSValueType.Binary -> {
                if ((kv.value is ByteArray)) {
                    val sksValue = SKSValue(kv.value, kv.type)
                    lookup[kv.key] = sksValue
                } else
                    throw SKSWriteException("value is not a valid SKSValueType.Binary type")
            }
            SKSValueType.Serialisable -> {
                val serialised = rss.serialiseData(kv.value)
                val sksValue = SKSValue(serialised, kv.type)
                lookup[kv.key] = sksValue
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
