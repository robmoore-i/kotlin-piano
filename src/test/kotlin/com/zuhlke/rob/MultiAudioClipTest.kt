package com.zuhlke.rob

import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class MultiAudioClipTest {
    private val mockPlayer = mockk<Player>(relaxed = true)
    private val mockClipA = mockk<Clip>(relaxed = true)
    private val mockClipB = mockk<Clip>(relaxed = true)

    @Test
    fun `when clip starts it plays all the sub clips`() {
        val multiAudioClip = MultiAudioClip(mockClipA, mockClipB)

        multiAudioClip.startWithPlaybackListener(mockPlayer)

        verify { mockPlayer.play(mockClipA) }
        verify { mockPlayer.play(mockClipB) }
    }
}