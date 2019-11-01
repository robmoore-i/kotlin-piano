package com.zuhlke.rob

import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import javax.sound.sampled.LineListener

class ClipPlayerTest {
    @Test
    fun `playing a clip starts it`() {
        val mockClip = mockk<AudioClip>(relaxed = true)
        val clipPlayer = ClipPlayer()

        clipPlayer.play(mockClip)

        verify { mockClip.play() }
    }
}

class ClipPlayer {
    fun play(clip: AudioClip) {
        clip.play()
    }
}

interface AudioClip : LineListener {
    fun play()
}
