package com.github.armay.okrandomaccess

import okio.BufferedSource
import okio.buffer

/**
 * This class is a buffered wrapper over a random access source.
 *
 * @param source The underlying source.
 */
class RandomAccessBufferedSource(source: RandomAccessSource) : RandomAccess by source, BufferedSource by source.buffer()