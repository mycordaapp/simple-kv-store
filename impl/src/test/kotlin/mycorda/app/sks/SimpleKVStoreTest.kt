package mycorda.app.sks

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import mycorda.app.types.UniqueId
import org.junit.jupiter.api.Test


class SimpleKVStoreTestTest {

    @Test
    fun `should store and read a value`() {
        val sks = SimpleKVStore()
        val textKey = Key("textKey")
        val binaryKey = Key("binaryKey")
        val objectKey = Key("objectKey")
        val someBinary = "hello world".toByteArray()

        sks.put(textKey, SKSValue("hello world", SKSValueType.Text))
        sks.put(binaryKey, SKSValue(someBinary, SKSValueType.Binary))
        sks.put(objectKey, SKSValue(Demo("abc", 123), SKSValueType.Serialisable))

        assertThat(sks.getText(textKey), equalTo("hello world"))
        assertThat(sks.getBinary(binaryKey).size, equalTo(someBinary.size))
        assertThat(sks.getDeserialised(objectKey), equalTo( Demo("abc", 123)))

    }

}