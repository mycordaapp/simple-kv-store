# Simple KV Store (sks)

[![Circle CI](https://circleci.com/gh/mycordaapp/simple-kv-store.svg?style=shield)](https://circleci.com/gh/mycordaapp/simple-kv-store)
[![Licence Status](https://img.shields.io/github/license/mycordaapp/simple-kv-store)](https://github.com/mycordaapp/simple-kv-store/blob/master/licence.txt)

## What it does

Simple KV Store is just a minimalist implementation of basic key-value store

Two implementations are provided:

* `SimpleKVStore` works in memory and is only intended for use within unit tests and examples.
* `FileKVStore` persists to file system. It is not intended for production usage.

The anticipation is that other implementations will be provided for production, for example a `JpaKVStore` that is
backed by a relational database. To support this the units will be refactored into a suite of common tests that should
pass for any event store.

By design this is JUST a key-value, with very limited searching. To efficiently 
find a record you *MUST* known the key beforehand.   

There payload can in 3 types
- A Kotlin class that corresponds to the rules of [Really Simple Serialisation(rss)](https://github.com/mycordaapp/really-simple-serialisation#readme).
- Plain text (CLOB)
- Binary (BLOB)


## Adding to a project 

Maven jars are deployed using [JitPack](https://jitpack.io/).
See [releases](https://github.com/mycordaapp/simple-kv-store/releases) for version details.

```groovy
//add jitpack repo
maven { url "https://jitpack.io" }

// add dependency 
implementation "com.github.mycordaapp:simple-kv-store:<release>"
```

_JitPack build status is at https://jitpack.io/com/github/mycordaapp/simple-kv-store/$releaseTag/build.log_

  
## Dependencies

As with everything in [myCorda dot App](https://mycorda.app), this library has minimal dependencies.

* Kotlin 1.4
* Java 11
* The object [Registry](https://github.com/mycordaapp/registry#readme)
* The [Really Simple Serialisation(rss)](https://github.com/mycordaapp/really-simple-serialisation#readme) module
    - [Jackson](https://github.com/FasterXML/jackson) for JSON serialisation

## Next Steps 

* [Using](docs/kv-store.md) the kv store.
