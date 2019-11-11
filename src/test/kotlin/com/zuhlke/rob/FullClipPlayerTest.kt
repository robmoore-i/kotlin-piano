package com.zuhlke.rob

import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import javax.sound.sampled.LineEvent

class FullClipPlayerTest {
    private val mockPlaybackLock = mockk<Lock>(relaxed = true)
    private val mockClip = mockk<Clip>(relaxed = true)

    @Before
    fun beforeEach() {
        clearMocks(mockPlaybackLock, mockClip)
    }

    @Test
    fun `starts sample when played using itself as the playback listener`() {
        val clipPlayer = FullClipPlayer(mockPlaybackLock)

        clipPlayer.play(mockClip)

        verify { mockClip.startWithPlaybackListener(clipPlayer) }
    }

    @Test
    fun `stops sample when stopped`() {
        val clipPlayer = FullClipPlayer(mockPlaybackLock)

        clipPlayer.play(mockClip)
        clipPlayer.update(lineEvent(LineEvent.Type.STOP))

        verify { mockClip.stop() }
    }

    @Test
    fun `when updated with a null event it does nothing`() {
        val clipPlayer = FullClipPlayer(mockPlaybackLock)

        clipPlayer.update(null)

        verify(exactly = 0) { mockPlaybackLock.release() }
    }

    @Test
    fun `when updated with a start event it does nothing`() {
        val clipPlayer = FullClipPlayer(mockPlaybackLock)

        clipPlayer.update(lineEvent(LineEvent.Type.START))

        verify(exactly = 0) { mockPlaybackLock.release() }
    }

    @Test
    fun `when updated with stop event it releases the playback lock`() {
        val clipPlayer = FullClipPlayer(mockPlaybackLock)

        clipPlayer.play(mockClip)
        clipPlayer.update(lineEvent(LineEvent.Type.STOP))

        verify { mockPlaybackLock.release() }
    }

    @Test
    fun `when the clip starts it blocks on the playback lock`() {
        val clipPlayer = FullClipPlayer(mockPlaybackLock)

        clipPlayer.play(mockClip)

        verify { mockPlaybackLock.block(any()) }
    }

    private fun lineEvent(type: LineEvent.Type): LineEvent {
        val stubLineEvent = mockk<LineEvent>()
        every { stubLineEvent.type } returns type
        return stubLineEvent
    }
}
