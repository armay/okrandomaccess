package com.github.armay.okrandomaccess

import okio.BufferedSink
import okio.buffer

/**
 * This class is a buffered wrapper over a random access sink.
 *
 * @param sink The underlying sink.
 */
class RandomAccessBufferedSink(sink: RandomAccessSink) : RandomAccess by sink, BufferedSink by sink.buffer()