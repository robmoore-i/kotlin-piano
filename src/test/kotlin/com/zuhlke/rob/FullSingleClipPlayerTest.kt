package com.zuhlke.rob

import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import javax.sound.sampled.LineEvent
import javax.sound.sampled.LineEvent.Type
import javax.sound.sampled.LineEvent.Type.START
import javax.sound.sampled.LineEvent.Type.STOP

class FullSingleClipPlayerTest {
    private val mockPlaybackLock = mockk<Lock>(relaxed = true)
    private val mockClip = mockk<SingleClip>(relaxed = true)

    @Before
    fun beforeEach() {
        clearMocks(mockPlaybackLock, mockClip)
    }

    @Test
    fun `when the clip stops it invokes the stop callbacks`() {
        val clipPlayer = FullSingleClipPlayer()
        val stopAction = mockk<() -> Unit>(relaxed = true)

        clipPlayer.addStopAction(stopAction)
        clipPlayer.play(mockClip, mockPlaybackLock)
        clipPlayer.onLineEvent(lineEvent(STOP))

        verify { stopAction.invoke() }
    }

    @Test
    fun `starts sample when played using itself as the playback listener`() {
        val clipPlayer = FullSingleClipPlayer()

        clipPlayer.play(mockClip, mockPlaybackLock)

        verify { mockClip.playUsing(clipPlayer) }
    }

    @Test
    fun `stops sample when stopped`() {
        val clipPlayer = FullSingleClipPlayer()

        clipPlayer.play(mockClip, mockPlaybackLock)
        clipPlayer.update(lineEvent(STOP))

        verify { mockClip.stop() }
    }

    @Test
    fun `when updated with a null event it does nothing`() {
        val clipPlayer = FullSingleClipPlayer()

        clipPlayer.update(null)

        verify(exactly = 0) { mockPlaybackLock.release() }
    }

    @Test
    fun `when updated with a start event it does nothing`() {
        val clipPlayer = FullSingleClipPlayer()

        clipPlayer.update(lineEvent(START))

        verify(exactly = 0) { mockPlaybackLock.release() }
    }

    @Test
    fun `when updated with stop event it releases the playback lock`() {
        val clipPlayer = FullSingleClipPlayer()

        clipPlayer.play(mockClip, mockPlaybackLock)
        clipPlayer.update(lineEvent(STOP))

        verify { mockPlaybackLock.release() }
    }

    @Test
    fun `when the clip starts it blocks on the playback lock`() {
        val clipPlayer = FullSingleClipPlayer()

        clipPlayer.play(mockClip, mockPlaybackLock)

        verify { mockPlaybackLock.block(any()) }
    }

    private fun lineEvent(type: Type): LineEvent {
        val stubLineEvent = mockk<LineEvent>()
        every { stubLineEvent.type } returns type
        return stubLineEvent
    }
}

