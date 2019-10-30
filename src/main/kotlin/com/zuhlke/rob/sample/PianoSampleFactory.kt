package com.zuhlke.rob.sample

import java.io.File

class PianoSampleFactory(private val pianoSamplesDirectory: File) {
    fun create(noteSpecification: String): PianoNoteSample {
        return PianoNoteSample(File(pianoSamplesDirectory, "$noteSpecification.aiff"))
    }
}
