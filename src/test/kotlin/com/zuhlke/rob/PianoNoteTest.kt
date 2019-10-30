package com.zuhlke.rob

import io.mockk.mockk
import io.mockk.verify
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.time.Duration
import java.time.temporal.ChronoUnit.SECONDS

class PianoNoteTest {
    @Test
    fun `play duration equals number of beats divided by beats per minute divided by seconds per minute`() {
        val dummySample = mockk<Sample>("piano sample")
        val pianoNote = PianoNote(dummySample, 1, 60)

        assertThat(pianoNote.duration(), equalTo(Duration.of(1, SECONDS)))
    }

    @Test
    fun `plays underlying sample to play the note`() {
        val mockSample = mockk<Sample>("piano sample", relaxed = true)
        val pianoNote = PianoNote(mockSample, 1, 60)

        pianoNote.play()

        verify { mockSample.play(any()) }
    }
}