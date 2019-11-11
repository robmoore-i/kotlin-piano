package com.zuhlke.rob

import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class MultiAudioClipTest {
    private val mockSemaphore = mockk<Semaphore>(relaxed = true)
    private val mockClipPlayer = mockk<SingleClipPlayer>(relaxed = true)
    private val mockClipA = mockk<SingleClip>(relaxed = true)
    private val mockClipB = mockk<SingleClip>(relaxed = true)

    @Before
    fun beforeEach() {
        clearMocks(mockSemaphore, mockClipPlayer, mockClipA, mockClipB)
    }

    @Test
    fun `when clips starts it registers itself as a clip stop listener`() {
        val multiAudioClip = MultiAudioClip(mockSemaphore, mockClipA, mockClipB)

        multiAudioClip.playUsing(mockClipPlayer)

        verify { mockClipPlayer.addStopAction(any()) }
    }

    @Test
    fun `it is complete when all the subclips are complete`() {
        every { mockClipA.isComplete() } returns true
        every { mockClipB.isComplete() } returns true
        val multiAudioClip = MultiAudioClip(mockSemaphore, mockClipA, mockClipB)

        assertTrue(multiAudioClip.isComplete())
    }

    @Test
    fun `it is initially incomplete`() {
        val multiAudioClip = MultiAudioClip(mockSemaphore, mockClipA, mockClipB)

        assertFalse(multiAudioClip.isComplete())
    }

    @Test
    fun `when clip stops it stops all the subclips`() {
        val multiAudioClip = MultiAudioClip(mockSemaphore, mockClipA, mockClipB)

        multiAudioClip.stop()

        verify { mockClipA.stop() }
        verify { mockClipA.stop() }
    }

    @Test
    fun `when clip starts it increments the semaphore by the number of subclips`() {
        val multiAudioClip = MultiAudioClip(mockSemaphore, mockClipA, mockClipB)

        multiAudioClip.playUsing(mockClipPlayer)

        verify { mockSemaphore.increment(2) }
    }

    @Test
    fun `when clip starts it plays all the subclips`() {
        val multiAudioClip = MultiAudioClip(mockSemaphore, mockClipA, mockClipB)

        multiAudioClip.playUsing(mockClipPlayer)

        verify { mockClipPlayer.play(mockClipA) }
        verify { mockClipPlayer.play(mockClipB) }
    }
}