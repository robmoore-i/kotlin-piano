package com.zuhlke.rob

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class MultiClipPlayerTest {
    private val mockMutliClip = mockk<MultiClip>(relaxed = true)
    private val mockSemaphore = mockk<Semaphore>(relaxed = true)
    private val singleClipPlayer = mockk<UniClipPlayer>(relaxed = true)

    @Test
    fun `it increments the semaphore by the cardinality of the subclip`() {
        val cardinality = 5
        every { mockMutliClip.cardinality } returns cardinality
        val fullMultiClipPlayer = MultiClipPlayer(mockSemaphore) { singleClipPlayer }

        fullMultiClipPlayer.play(mockMutliClip)

        verify { mockSemaphore.increment(cardinality) }
    }

    @Test
    fun `it plays the multiclip`() {
        val fullMultiClipPlayer = MultiClipPlayer(mockSemaphore) { singleClipPlayer }

        fullMultiClipPlayer.play(mockMutliClip)

        verify { mockMutliClip.playUsing(any()) }
    }

    @Test
    fun `when clip starts it blocks on the semaphore`() {
        val fullMultiClipPlayer = MultiClipPlayer(mockSemaphore) { singleClipPlayer }

        fullMultiClipPlayer.play(mockMutliClip)

        verify { mockSemaphore.block() }
    }
}