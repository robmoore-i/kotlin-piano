package com.zuhlke.rob

import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class MultiAudioClipTest {
    private val mockSemaphore = mockk<Semaphore>(relaxed = true)
    private val mockPlayer = mockk<ClipPlayer>(relaxed = true)
    private val mockClipA = mockk<Clip>(relaxed = true)
    private val mockClipB = mockk<Clip>(relaxed = true)

    @Test
    fun `when clip starts it increments the semaphore by the number of clips`() {
        val multiAudioClip = MultiAudioClip(mockSemaphore, mockClipA, mockClipB)

        multiAudioClip.startWithPlaybackListener(mockPlayer)

        verify { mockSemaphore.increment(2) }
    }

    @Test
    fun `when clip starts it plays all the sub clips`() {
        val multiAudioClip = MultiAudioClip(mockSemaphore, mockClipA, mockClipB)

        multiAudioClip.startWithPlaybackListener(mockPlayer)

        verify { mockPlayer.play(mockClipA) }
        verify { mockPlayer.play(mockClipB) }
    }
}