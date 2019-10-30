package com.zuhlke.rob

import java.io.File
import java.time.Duration
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip
import javax.sound.sampled.DataLine

class PianoSample(private val audioFile: File) : Sample {
    override fun play() {
        playSample(FullClipPlayer())
    }

    override fun play(duration: Duration) {
        playSample(CutoffClipPlayer(duration))
    }

    private fun playSample(clipPlayer: ClipPlayer) {
        withClip(clipPlayer) { clip ->
            val playbackLock: PlaybackLock = clipPlayer.playbackLock()
            clip.start()
            playbackLock.blockUntil { clipPlayer.isCompleted() }
        }
    }

    private fun withClip(clipPlayer: ClipPlayer, function: (clip: Clip) -> Unit) {
        val audioInputStream = AudioSystem.getAudioInputStream(audioFile)
        val clip = AudioSystem.getLine(DataLine.Info(Clip::class.java, audioInputStream.format)) as Clip
        clip.addLineListener(clipPlayer)
        clip.open(audioInputStream)
        function.invoke(clip)
        clip.close()
        audioInputStream.close()
    }
}
