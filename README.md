OkRandomAccess
==============

OkRandomAccess is a small extension for [Okio](https://square.github.io/okio/) 
introducing random access functionality to sinks and sources on the JVM.

Motivation
----------

It looks like dealing with random access files will become possible with Okio 3.
However, this library
- is compatible with Okio 2 while Okio 3 is still in alpha;
- provides the simplest possible API via position parameter avoiding artificial entities like cursor from Okio 3.

Build
-----

This software is built with [Gradle](https://gradle.org) and tested to work with Java 8 and 15.
Testing is done with [Jimfs](https://github.com/google/jimfs) and [JUnit](https://junit.org/junit5/).

Limitations
-----------

Currently, only reading from and writing to a file are supported.
For additional functionality feel free to open an issue/discussion.

The target environment for this library is JVM. 
Multiplatform support is beyond the scope of this library.

Usage
-----

Creating a random access sink/source is done similarly to Okio's `source()`/`sink()` API:
```kotlin
import com.github.armay.okrandomaccess.buffer
import com.github.armay.okrandomaccess.randomAccessSink
import com.github.armay.okrandomaccess.randomAccessSource

/* Other imports & code */

val path: Path = Paths.get("some_file")

path.randomAccessSource().buffer().use { source -> /* Your code goes here */ }

path.randomAccessSink().buffer().use { sink -> /* Your code goes here */ }
```
Calling `buffer()` wraps a `RandomAccessSink` or `RandomAccessSource` into its buffered version
making accessible methods such as `BufferedSource.readInt(): Int`, etc.

Positioning is done via `RandomAccess.position: Long` property, e.g.
```kotlin
    source.position = i * bytesPerElement // point at the i-th element in a file representation of a list
```

Please, refer to [tests](lib/src/test/kotlin/com/github/armay/okrandomaccess/OkRandomAccessTest.kt) 
for a working code sample.

License
-------

This project is released under [MIT License](LICENSE.md).
