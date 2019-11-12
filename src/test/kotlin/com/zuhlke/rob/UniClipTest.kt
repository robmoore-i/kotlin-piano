package com.zuhlke.rob

import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.LineEvent

class UniClipTest {
    private val mockAudioInputStream = mockk<AudioInputStream>(relaxed = true)
    private val mockRawClip = mockk<RawClip>(relaxed = true)
    private val mockLock = mockk<Lock>(relaxed = true)

    @Before
    fun beforeEach() {
        clearMocks(mockAudioInputStream, mockRawClip, mockLock)
    }

    @Test
    fun `when the clip stops it invokes the stop callbacks`() {
        val stopAction = mockk<() -> Unit>(relaxed = true)
        val clip = UniClip(mockAudioInputStream, mockRawClip)

        clip.addStopAction(stopAction)
        clip.playInForeground(mockLock)
        clip.update(lineEvent(LineEvent.Type.STOP))

        verify { stopAction.invoke() }
    }

    @Test
    fun `when played, it starts raw clip and opens audio input stream`() {
        val clip = UniClip(mockAudioInputStream, mockRawClip)

        clip.playInForeground(mockLock)

        verify { mockRawClip.start() }
        verify { mockRawClip.open(mockAudioInputStream) }
    }

    @Test
    fun `when stopped it closes the raw clip and the audio input stream`() {
        val clip = UniClip(mockAudioInputStream, mockRawClip)

        clip.playInForeground(mockLock)
        clip.update(lineEvent(LineEvent.Type.STOP))

        verify { mockRawClip.close() }
        verify { mockAudioInputStream.close() }
    }

    @Test
    fun `when updated with a start event it does nothing`() {
        val clip = UniClip(mockAudioInputStream, mockRawClip)

        clip.update(lineEvent(LineEvent.Type.START))

        verify(exactly = 0) { mockLock.release() }
    }

    @Test
    fun `after starting in the foreground, when updated with stop event, it releases the lock`() {
        val clip = UniClip(mockAudioInputStream, mockRawClip)

        clip.playInForeground(mockLock)
        clip.update(lineEvent(LineEvent.Type.STOP))

        verify { mockLock.release() }
    }

    @Test
    fun `when the clip starts in the foreground it blocks on the lock`() {
        val clip = UniClip(mockAudioInputStream, mockRawClip)

        clip.playInForeground(mockLock)

        verify { mockLock.block(any()) }
    }

    @Test(expected = RuntimeException::class)
    fun `once stopped it cannot be restarted`() {
        val clip = UniClip(mockAudioInputStream, mockRawClip)

        clip.stop()

        clip.playInBackground()
    }

    @Test
    fun `when stopped it becomes complete`() {
        val clip = UniClip(mockAudioInputStream, mockRawClip)

        clip.playInForeground(mockLock)
        clip.stop()

        assertTrue(clip.isComplete())
    }

    @Test
    fun `is initially not completed`() {
        val clip = UniClip(mockAudioInputStream, mockRawClip)

        assertFalse(clip.isComplete())
    }

    private fun lineEvent(type: LineEvent.Type): LineEvent {
        val stubLineEvent = mockk<LineEvent>()
        every { stubLineEvent.type } returns type
        return stubLineEvent
    }
}

