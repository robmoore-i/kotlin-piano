package com.zuhlke.rob

import java.time.Duration
import java.time.temporal.ChronoUnit

class PianoNote(private val sample: Sample, private val numberOfBeats: Int, private val bpm: Int) {
    fun duration(): Duration {
        val numberOfSeconds = numberOfBeats / (bpm / 60)
        return Duration.of(numberOfSeconds.toLong(), ChronoUnit.SECONDS)
    }

    fun play() {
        sample.play(duration())
    }
}
