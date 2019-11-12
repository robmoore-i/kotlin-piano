package com.zuhlke.rob

import javax.sound.sampled.AudioInputStream

typealias RawClip = javax.sound.sampled.Clip

interface UniClip : Clip<UniClipPlayer> {
    override fun cardinality() = 1
}

class UniAudioClip(private val audioInputStream: AudioInputStream, private val clip: RawClip) : UniClip {
    private var complete: Boolean = false

    override fun playUsing(player: UniClipPlayer) {
        if (complete) {
            throw RuntimeException("Clip is already completed")
        }
        clip.addLineListener(player)
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