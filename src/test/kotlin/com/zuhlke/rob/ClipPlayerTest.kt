package com.zuhlke.rob

import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import javax.sound.sampled.LineEvent
import javax.sound.sampled.LineListener

class ClipPlayerTest {
    private val mockPlaybackLock = mockk<PlaybackLock>(relaxed = true)
    private val mockSample = mockk<Clip>(relaxed = true)

    @Before
    fun setUp() {
        clearMocks(mockPlaybackLock, mockSample)
    }

    @Test
    fun `when the clip starts it blocks on the playback lock`() {
        val clipPlayer = ClipPlayer(mockPlaybackLock)

        clipPlayer.play(mockSample)

        verify { mockPlaybackLock.block() }
    }

    @Test
    fun `when updated with stop event it releases the playback lock`() {
        val clipPlayer = ClipPlayer(mockPlaybackLock)

        clipPlayer.update(lineEvent(LineEvent.Type.STOP))

        verify { mockPlaybackLock.release() }
    }

    @Test
    fun `when updated with a start event it does nothing`() {
        val clipPlayer = ClipPlayer(mockPlaybackLock)

        clipPlayer.update(lineEvent(LineEvent.Type.START))

        verify(exactly = 0) { mockPlaybackLock.release() }
    }

    @Test
    fun `when updated with a null event it does nothing`() {
        val clipPlayer = ClipPlayer(mockPlaybackLock)

        clipPlayer.update(null)

        verify(exactly = 0) { mockPlaybackLock.release() }
    }

    @Test
    fun `stops sample when stopped`() {
        val clipPlayer = ClipPlayer(mockPlaybackLock)

        clipPlayer.play(mockSample)
        clipPlayer.update(lineEvent(LineEvent.Type.STOP))

        verify { mockSample.stop() }
    }

    @Test
    fun `starts sample when played using itself as the playback listener`() {
        val clipPlayer = ClipPlayer(mockPlaybackLock)

        clipPlayer.play(mockSample)

        verify { mockSample.startWithPlaybackListener(clipPlayer) }
    }

    private fun lineEvent(type: LineEvent.Type): LineEvent {
        val stubLineEvent = mockk<LineEvent>()
        every { stubLineEvent.type } returns type
        return stubLineEvent
    }
}

interface Player : LineListener {
    fun play(clip: Clip)
}

interface Clip {
    fun startWithPlaybackListener(player: Player)
    fun stop()
}

interface PlaybackLock {
    fun block()
    fun release()
}

class ClipPlayer(private val playbackLock: PlaybackLock) : Player {
    private lateinit var clip: Clip

    override fun update(event: LineEvent?) {
        if (event == null) {
            return
        }
        if (event.type == LineEvent.Type.STOP) {
            onStop()
        }
    }

    override fun play(clip: Clip) {
        this.clip = clip
        clip.startWithPlaybackListener(this)
        playbackLock.block()
    }

    private fun onStop() {
        clip.stop()
        playbackLock.release()
    }
}
