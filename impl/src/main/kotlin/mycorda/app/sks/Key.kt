package mycorda.app.sks

import mycorda.app.helpers.random
import mycorda.app.types.UniqueId
import java.util.*

open class Key(val key: String) {
    init {
        // set some basic rules length rules
        if (key.isEmpty()) throw RuntimeException("Key cannot be empty")
        if (key.length > 255) throw RuntimeException("Key must be 255 characters or less")
        if (!pattern.matches(key)) throw RuntimeException("Invalid characters. Only alphanumeric, slash(/), hyphen (-), underscore (_) and colon(:) supported")
    }

    companion object {
        val pattern = Regex("^[a-zA-Z0-9_:\\-//]+\$")

        /**
         * From a random UUID
         */
        fun randomUUID(): UniqueId {
            return UniqueId(UUID.randomUUID().toString())
        }

        /**
         * From a provided String. Max length is 255
         */
        fun fromString(id: String): Key {
            return Key(id)
        }

        /**
         * From a provided UUID
         */
        fun fromUUID(id: UUID): Key {
            return Key(id.toString())
        }


        /**
         * Build a random string in a 'booking reference' style,
         * e.g. `BZ13FG`
         */
        fun alphanumeric(length: Int = 6): Key {
            return Key(String.random(length))
        }
    }

    override fun equals(other: Any?): Boolean {
        return if (other is Key) {
            this.key == other.key
        } else false
    }

    override fun hashCode(): Int = key.hashCode()

    override fun toString(): String = key

}