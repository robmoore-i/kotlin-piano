package com.zuhlke.rob

import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class MultiClipTest {
    private val mockSemaphore = mockk<Semaphore>(relaxed = true)
    private val mockClipA = mockk<UniClip>(relaxed = true)
    private val mockClipB = mockk<UniClip>(relaxed = true)

    @Before
    fun beforeEach() {
        clearMocks(mockSemaphore, mockClipA, mockClipB)
    }

    @Test
    fun `it increments the semaphore by the cardinality of the subclip`() {
        val multiAudioClip = MultiClip(mockClipA, mockClipB)

        multiAudioClip.playInForeground(mockSemaphore)

        verify { mockSemaphore.increment(2) }
    }

    @Test
    fun `when clip starts it blocks on the semaphore`() {
        val multiAudioClip = MultiClip(mockClipA, mockClipB)

        multiAudioClip.playInForeground(mockSemaphore)

        verify { mockSemaphore.block() }
    }

    @Test
    fun `it is complete when all the subclips are complete`() {
        every { mockClipA.isComplete() } returns true
        every { mockClipB.isComplete() } returns true
        val multiAudioClip = MultiClip(mockClipA, mockClipB)

        assertTrue(multiAudioClip.isComplete())
    }

    @Test
    fun `it is initially incomplete`() {
        val multiAudioClip = MultiClip(mockClipA, mockClipB)

        assertFalse(multiAudioClip.isComplete())
    }

    @Test
    fun `when clip stops it stops all the subclips`() {
        val multiAudioClip = MultiClip(mockClipA, mockClipB)

        multiAudioClip.stop()

        verify { mockClipA.stop() }
        verify { mockClipA.stop() }
    }

    @Test
    fun `when clip starts it plays all the subclips in the background`() {
        val multiAudioClip = MultiClip(mockClipA, mockClipB)

        multiAudioClip.playInForeground(mockSemaphore)

        verify { mockClipA.playInBackground() }
        verify { mockClipB.playInBackground() }
    }
}