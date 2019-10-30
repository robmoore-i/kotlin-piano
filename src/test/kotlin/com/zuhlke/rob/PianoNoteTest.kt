package com.zuhlke.rob

import io.mockk.mockk
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.time.Duration
import java.time.temporal.ChronoUnit.*

class PianoNoteTest {
    @Test
    fun `play duration equals number of beats divided by beats per minute divided by seconds per minute`() {
        val mockSample = mockk<Sample>("piano sample")
        val pianoNote = PianoNote(mockSample, 1, 60)

        assertThat(pianoNote.duration(), equalTo(Duration.of(1, SECONDS)))
    }
}