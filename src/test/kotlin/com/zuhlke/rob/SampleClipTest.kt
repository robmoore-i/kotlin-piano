package com.zuhlke.rob

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import javax.sound.sampled.LineEvent

class SampleClipTest {
    @Test
    fun `when the clip starts it blocks on the playback lock`() {
        val mockPlaybackLock = mockk<PlaybackLock>(relaxed = true)
        val clip = SampleClip(mockPlaybackLock)

        clip.play()

        verify { mockPlaybackLock.block() }
    }

    @Test
    fun `when updated with stop event it releases the playback lock`() {
        val mockPlaybackLock = mockk<PlaybackLock>(relaxed = true)
        val clip = SampleClip(mockPlaybackLock)

        clip.update(lineEvent(LineEvent.Type.STOP))

        verify { mockPlaybackLock.release() }
    }

    @Test
    fun `when updated with a start event it does nothing`() {
        val mockPlaybackLock = mockk<PlaybackLock>(relaxed = true)
        val clip = SampleClip(mockPlaybackLock)

        clip.update(lineEvent(LineEvent.Type.START))

        verify(exactly = 0) { mockPlaybackLock.release() }
    }

    @Test
    fun `when updated with a null event it does nothing`() {
        val mockPlaybackLock = mockk<PlaybackLock>(relaxed = true)
        val clip = SampleClip(mockPlaybackLock)

        clip.update(null)

        verify(exactly = 0) { mockPlaybackLock.release() }
    }

    private fun lineEvent(type: LineEvent.Type): LineEvent {
        val stubLineEvent = mockk<LineEvent>()
        every { stubLineEvent.type } returns type
        return stubLineEvent
    }
}

interface PlaybackLock {
    fun block()
    fun release()
}

class SampleClip(private val playbackLock: PlaybackLock) : AudioClip {
    override fun update(event: LineEvent?) {
        if (event == null) {
            return
        }
        if (event.type == LineEvent.Type.STOP) {
            onStop()
        }
    }

    override fun play() {
        playbackLock.block()
    }

    private fun onStop() {
        playbackLock.release()
    }
}
