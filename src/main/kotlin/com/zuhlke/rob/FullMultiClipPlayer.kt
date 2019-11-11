package com.zuhlke.rob

class FullMultiClipPlayer(private val semaphore: Semaphore, private val singleClipPlayer: SingleClipPlayer) {
    fun play(mutliClip: MultiClip) {
        semaphore.increment(mutliClip.cardinality)
        singleClipPlayer.addStopAction { semaphore.decrement(1) }
        mutliClip.playUsing(singleClipPlayer)
        semaphore.block()
    }
}
