package com.github.armay.okrandomaccess

import okio.Buffer
import okio.Timeout.Companion.NONE
import java.nio.channels.FileChannel.open
import java.nio.file.Files.exists
import java.nio.file.Files.isReadable
import java.nio.file.Path
import java.nio.file.StandardOpenOption.READ
import java.util.*

/**
 * Random access file source implementation.
 * The underlying buffer is cleared on each position change.
 *
 * @param path Path to the random access file (must exist; must be readable).
 */
internal class RandomAccessFileSource(path: Path) : RandomAccessSource {
    init { require(exists(path) && isReadable(path)) }

    private var buffer = Buffer()
    private val channel = open(path, READ_ONLY)
    private var _position = 0L

    /**
     * The file-pointer offset, measured in bytes from the beginning of the file, at which the next read occurs.
     * Its value should never go beyond the current file limits.
     */
    override var position
        get() = _position
        set(value) {
            require(value in 0 until channel.size())
            if (value != _position) {
                _position = value
                buffer.clear()
            }
        }

    override fun close() {
        channel.close()
    }

    override fun read(sink: Buffer, byteCount: Long): Long {
        check(channel.isOpen)
        if (_position == channel.size()) return -1L
        val bytesRead = channel.transferTo(_position, byteCount, sink)
        _position += bytesRead
        buffer = sink
        return bytesRead
    }

    override fun timeout() = NONE

    companion object {
        private val READ_ONLY = EnumSet.of(READ)
    }
}