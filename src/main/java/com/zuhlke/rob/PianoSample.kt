package com.zuhlke.rob

import java.io.File
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip
import javax.sound.sampled.DataLine

class PianoSample(private val audioFile: File) {
    fun play() {
        withOpenClip { clip ->
            val clipLineListener = ClipLineListener()
            val playbackLock: Object = clipLineListener.playbackLock() as Object;
            clip.addLineListener(clipLineListener)
            clip.start()
            while (!clipLineListener.isCompleted) {
                try {
                    playbackLock.wait()
                } catch (ignored: IllegalMonitorStateException) {
                }
            }
        }
    }

    private fun withOpenClip(function: (clip: Clip) -> Unit) {
        val audioInputStream = AudioSystem.getAudioInputStream(audioFile)
        val clip = AudioSystem.getLine(DataLine.Info(Clip::class.java, audioInputStream.format)) as Clip
        clip.open(audioInputStream)
        function.invoke(clip)
        clip.close()
        audioInputStream.close()
    }
}
