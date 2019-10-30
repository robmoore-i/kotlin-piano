package com.zuhlke.rob.sample

import java.io.File
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip
import javax.sound.sampled.DataLine

class PianoSampleFactory(private val pianoSamplesDirectory: File) {
    fun create(noteSpecification: String): PianoNoteSample {
        val audioInputStream = AudioSystem.getAudioInputStream(File(pianoSamplesDirectory, "$noteSpecification.aiff"))
        val clip = AudioSystem.getLine(DataLine.Info(Clip::class.java, audioInputStream.format)) as Clip
        return PianoNoteSample(AudioClip(audioInputStream, clip))
    }
}
