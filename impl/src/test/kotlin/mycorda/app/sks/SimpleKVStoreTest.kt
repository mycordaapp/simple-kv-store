package mycorda.app.sks

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
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

        assertThat(sks.get(textKey).toText(), equalTo("hello world"))
        assertThat(sks.get(binaryKey).toBinary().size, equalTo(someBinary.size))
        assertThat(sks.deserialise(sks.get(objectKey)), equalTo(Demo("abc", 123)))
    }

    @Test
    fun `should read by prefix`() {
        val sks = SimpleKVStore()

        sks.put(Key("foo1"), SKSValue("bar1", SKSValueType.Text))
        sks.put(Key("foo"), SKSValue("bar", SKSValueType.Text))
        sks.put(Key("foo2"), SKSValue("bar2", SKSValueType.Text))
        sks.put(Key("etcd"), SKSValue("etcd", SKSValueType.Text))

        val filtered = sks.getList(Key("foo")).toList()
        assertThat(filtered.size, equalTo(3))
        assertThat(filtered.map { it.key.key }, equalTo(listOf("foo", "foo1", "foo2")))
    }

}