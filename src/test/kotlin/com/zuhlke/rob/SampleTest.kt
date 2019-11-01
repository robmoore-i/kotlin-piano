package com.zuhlke.rob

import org.junit.Test
import java.io.File
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip
import javax.sound.sampled.DataLine

class SampleTest {
    @Test
    fun `plays a sample`() {
        val audioInputStream = AudioSystem.getAudioInputStream(File("/Users/romo/IdeaProjects/sounds/src/main/resources/iowa", "ff3C.aiff"))
        val clip = AudioSystem.getLine(DataLine.Info(Clip::class.java, audioInputStream.format)) as Clip
        val sample = AudioSample(audioInputStream, clip)
    }
}

class AudioSample(audioInputStream: AudioInputStream, clip: Clip) {
}
