package com.zuhlke.rob

import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import javax.sound.sampled.LineEvent

class SampleClipTest {
    private val mockPlaybackLock = mockk<PlaybackLock>(relaxed = true)
    private val mockSample = mockk<Sample>(relaxed = true)

    @Before
    fun setUp() {
        clearMocks(mockPlaybackLock, mockSample)
    }

    @Test
    fun `when the clip starts it blocks on the playback lock`() {
        val clip = SampleClip(mockPlaybackLock, mockSample)

        clip.play()

        verify { mockPlaybackLock.block() }
    }

    @Test
    fun `when updated with stop event it releases the playback lock`() {
        val clip = SampleClip(mockPlaybackLock, mockSample)

        clip.update(lineEvent(LineEvent.Type.STOP))

        verify { mockPlaybackLock.release() }
    }

    @Test
    fun `when updated with a start event it does nothing`() {
        val clip = SampleClip(mockPlaybackLock, mockSample)

        clip.update(lineEvent(LineEvent.Type.START))

        verify(exactly = 0) { mockPlaybackLock.release() }
    }

    @Test
    fun `when updated with a null event it does nothing`() {
        val clip = SampleClip(mockPlaybackLock, mockSample)

        clip.update(null)

        verify(exactly = 0) { mockPlaybackLock.release() }
    }

    @Test
    fun `stops sample when stopped`() {
        val clip = SampleClip(mockPlaybackLock, mockSample)

        clip.update(lineEvent(LineEvent.Type.STOP))

        verify { mockSample.stop() }
    }

    @Test
    fun `starts sample when played using itself as the playback listener`() {
        val clip = SampleClip(mockPlaybackLock, mockSample)

        clip.play()

        verify { mockSample.startWithPlaybackListener(clip) }
    }

    private fun lineEvent(type: LineEvent.Type): LineEvent {
        val stubLineEvent = mockk<LineEvent>()
        every { stubLineEvent.type } returns type
        return stubLineEvent
    }
}

interface Sample {
    fun startWithPlaybackListener(clip: AudioClip)
    fun stop()
}

interface PlaybackLock {
    fun block()
    fun release()
}

class SampleClip(private val playbackLock: PlaybackLock, private val sample: Sample) : AudioClip {
    override fun update(event: LineEvent?) {
        if (event == null) {
            return
        }
        if (event.type == LineEvent.Type.STOP) {
            onStop()
        }
    }

    override fun play() {
        sample.startWithPlaybackListener(this)
        playbackLock.block()
    }

    private fun onStop() {
        sample.stop()
        playbackLock.release()
    }
}
