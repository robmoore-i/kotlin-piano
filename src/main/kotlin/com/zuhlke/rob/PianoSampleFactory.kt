package com.zuhlke.rob

import java.io.File

class PianoSampleFactory(private val pianoSamplesDirectory: File) {
    fun create(noteSpecification: String): PianoSample {
        return PianoSample(File(pianoSamplesDirectory, "$noteSpecification.aiff"))
    }
}
