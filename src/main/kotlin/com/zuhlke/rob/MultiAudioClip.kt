package com.zuhlke.rob

interface MultiClip {
    fun playUsing(singleClipPlayer: SingleClipPlayer)
    fun stop()
    fun isComplete(): Boolean
}

class MultiAudioClip(private val semaphore: Semaphore, private vararg val subclips: SingleClip) : MultiClip {
    override fun playUsing(singleClipPlayer: SingleClipPlayer) {
        semaphore.increment(subclips.size)
        singleClipPlayer.addStopAction { semaphore.decrement(1) }
        subclips.forEach { singleClipPlayer.play(it) }
    }

    override fun stop() {
        subclips.forEach { it.stop() }
    }

    override fun isComplete(): Boolean {
        return subclips.all { it.isComplete() }
    }
}