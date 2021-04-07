package com.github.armay.okrandomaccess

import okio.Buffer
import okio.Timeout.Companion.NONE
import java.nio.channels.FileChannel.open
import java.nio.file.Files.*
import java.nio.file.Path
import java.nio.file.StandardOpenOption.READ
import java.nio.file.StandardOpenOption.WRITE
import java.util.*

/**
 * Random access file sink implementation.
 * The underlying buffer is cleared on each position change.
 *
 * @param path Path to the random access file (created if not exists; must be writable).
 */
internal class RandomAccessFileSink(path: Path) : RandomAccessSink {
    init {
        if (notExists(path)) createFile(path)
        require(exists(path) && isWritable(path))
    }

    private val channel = open(path, READ_WRITE)
    private var buffer = Buffer()
    private var _position = 0L

    /**
     * The file-pointer offset, measured in bytes from the beginning of the file, at which the next write occurs.
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

    override fun flush() {
        check(channel.isOpen)
        val bytesWritten = channel.transferFrom(buffer, _position, buffer.size)
        _position += bytesWritten
        buffer.clear()
    }

    override fun timeout() = NONE

    override fun write(source: Buffer, byteCount: Long) {
        check(channel.isOpen)
        val bytesWritten = channel.transferFrom(source, _position, byteCount)
        _position += bytesWritten
        buffer = source
    }

    companion object {
        private val READ_WRITE = EnumSet.of(READ, WRITE)
    }
}