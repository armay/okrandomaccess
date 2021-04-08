package com.github.armay.okrandomaccess

import java.nio.file.Path

/**
 * Creates a new random access file sink from a given path.
 */
fun Path.randomAccessSink(): RandomAccessSink = RandomAccessFileSink(this)

/**
 * Creates a new random access file source from a given path.
 */
fun Path.randomAccessSource(): RandomAccessSource = RandomAccessFileSource(this)

/**
 * Creates a new buffered random access sink from a given random access sink.
 */
fun RandomAccessSink.buffer() = RandomAccessBufferedSink(this)

/**
 * Creates a new buffered random access source from a given random access source.
 */
fun RandomAccessSource.buffer() = RandomAccessBufferedSource(this)
