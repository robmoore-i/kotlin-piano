package com.zuhlke.rob.sample

import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class PianoNoteSampleTest {
    @Test
    fun `when the sample is played, the current thread is blocked`() {
        val pianoNoteSample = PianoNoteSample(mockk("audio clip", relaxed = true))
        val mockPlaybackLock = mockk<PlaybackLock>("playback lock", relaxed = true)
        val stubClipPlayer = TestClipPlayer(mockPlaybackLock)

        pianoNoteSample.playWith(stubClipPlayer)

        verify { mockPlaybackLock.blockUntil(any()) }
    }
}