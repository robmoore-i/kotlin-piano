package com.zuhlke.rob

import org.junit.Test
import java.io.File
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.DataLine

typealias RawClip = javax.sound.sampled.Clip

class ClipTest {
    @Test
    fun `can be played by a clip player`() {
        val audioInputStream = AudioSystem.getAudioInputStream(
            File(
                "/Users/romo/IdeaProjects/sounds/src/main/resources/iowa",
                "ff3C.aiff"
            )
        )
        val rawClip = AudioSystem.getLine(DataLine.Info(RawClip::class.java, audioInputStream.format)) as RawClip
        val clip = AudioClip(audioInputStream, rawClip)

        val clipPlayer = ClipPlayer(ObjectPlaybackLock())

//        clip.startWithPlaybackListener(clipPlayer)
    }
}

class AudioClip(private val audioInputStream: AudioInputStream, private val clip: RawClip) : Clip {
    override fun startWithPlaybackListener(player: Player) {
        clip.addLineListener(player)
        clip.open(audioInputStream)
        clip.start()
    }

    override fun stop() {
        clip.close()
        audioInputStream.close()
    }
}

class ObjectPlaybackLock : PlaybackLock {
    override fun block() {
    }

    override fun release() {
    }
}