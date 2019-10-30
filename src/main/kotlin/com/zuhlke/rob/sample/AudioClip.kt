package com.zuhlke.rob.sample

import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.Clip

class AudioClip(private val audioInputStream: AudioInputStream, private val clip: Clip) {
    fun open() {
        clip.open(audioInputStream)
    }

    fun start() {
        clip.start()
    }

    fun close() {
        clip.close()
        audioInputStream.close()
    }

    fun addPlayer(clipPlayer: ClipPlayer) {
        clip.addLineListener(clipPlayer)
    }
}