package com.zuhlke.rob

import io.mockk.mockk
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import javax.sound.sampled.AudioInputStream

class AudioClipTest {
    private val mockAudioInputStream = mockk<AudioInputStream>(relaxed = true)
    private val mockRawClip = mockk<RawClip>(relaxed = true)

    @Test(expected = RuntimeException::class)
    fun `once stopped it cannot be restarted`() {
        val clip = AudioClip(mockAudioInputStream, mockRawClip)

        clip.stop()

        clip.playUsing(mockk())
    }

    @Test
    fun `when stopped it becomes complete`() {
        val clip = AudioClip(mockAudioInputStream, mockRawClip)

        clip.stop()

        assertTrue(clip.isComplete())
    }

    @Test
    fun `is initially not completed`() {
        val clip = AudioClip(mockAudioInputStream, mockRawClip)

        assertFalse(clip.isComplete())
    }
}

