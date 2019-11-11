package com.zuhlke.rob

import javax.sound.sampled.AudioInputStream

typealias RawClip = javax.sound.sampled.Clip

interface Clip {
    fun playUsing(singleClipPlayer: SingleClipPlayer)
    fun stop()
    fun isComplete(): Boolean
}

class AudioClip(private val audioInputStream: AudioInputStream, private val clip: RawClip) : Clip {
    private var complete: Boolean = false

    override fun playUsing(singleClipPlayer: SingleClipPlayer) {
        if (complete) {
            throw RuntimeException("Clip is already completed")
        }
        clip.addLineListener(singleClipPlayer)
        clip.open(audioInputStream)
        clip.start()
    }

    override fun stop() {
        clip.close()
        audioInputStream.close()
        complete = true
    }

    override fun isComplete(): Boolean {
        return complete
    }
}

class MultiAudioClip(private val semaphore: Semaphore, private vararg val subclips: Clip) : Clip {
    override fun playUsing(singleClipPlayer: SingleClipPlayer) {
        semaphore.increment(subclips.size)
        subclips.forEach { singleClipPlayer.play(it) }
    }

    override fun stop() {
        subclips.forEach { it.stop() }
    }

    override fun isComplete(): Boolean {
        return subclips.all { it.isComplete() }
    }
}