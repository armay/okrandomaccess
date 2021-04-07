package com.github.armay.okrandomaccess

import com.github.armay.okrandomaccess.OkRandomAccess.buffer
import com.github.armay.okrandomaccess.OkRandomAccess.randomAccessSink
import com.github.armay.okrandomaccess.OkRandomAccess.randomAccessSource
import com.google.common.jimfs.Configuration.unix
import com.google.common.jimfs.Jimfs.newFileSystem
import okio.buffer
import okio.sink
import okio.source
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertTrue

class OkRandomAccessTest {
    private val data = listOf(4, 9, 2, 3, 5, 7, 8, 1, 6)
    private val bytesPerElement = 4L

    @Test fun `Random access source`() { newFileSystem(unix()).use { fs ->
        val path = fs.getPath("test_input")
        path.sink().buffer().use { sink -> data.forEach { sink.writeInt(it) } }
        path.randomAccessSource().buffer().use { source ->
            for (i in data.indices) {
                assertEquals(data[i], source.readInt())
            }
            assertTrue { source.exhausted() }
            for (i in data.indices.reversed()) {
                source.position = i * bytesPerElement
                assertEquals(data[i], source.readInt())
            }
            assertFails { source.position = data.size * bytesPerElement }
        }
    } }

    @Test fun `Random access sink`() { newFileSystem(unix()).use { fs ->
        val path = fs.getPath("test_output")
        path.randomAccessSink().buffer().use { sink ->
            for (i in data.indices) {
                sink.writeInt(data[i])
            }
            assertFails { sink.position = data.size * bytesPerElement }
        }
        path.source().buffer().use { source ->
            for (i in data.indices) {
                assertEquals(data[i], source.readInt())
            }
        }
        path.randomAccessSink().buffer().use { sink ->
            for (i in data.indices.reversed()) {
                sink.position = i * bytesPerElement
                sink.writeInt(data[i])
            }
        }
        path.source().buffer().use { source ->
            for (i in data.indices.reversed()) {
                assertEquals(data[i], source.readInt())
            }
            assertTrue { source.exhausted() }
        }
    } }
}
