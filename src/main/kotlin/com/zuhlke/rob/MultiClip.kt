package com.zuhlke.rob

class MultiClip(private vararg val subclips: UniClip) : Clip<() -> UniClipPlayer> {
    override fun cardinality(): Int = subclips.size

    override fun playUsing(player: () -> UniClipPlayer) {
        subclips.forEach { player.invoke().playInBackground(it) }
    }

    override fun stop() {
        subclips.forEach { it.stop() }
    }

    override fun isComplete(): Boolean {
        return subclips.all { it.isComplete() }
    }


    fun play(mutex: Semaphore, uniClipPlayerProvider: () -> UniClipPlayer) {
        mutex.increment(cardinality())
        playUsing {
            val singleClipPlayer = uniClipPlayerProvider.invoke()
            singleClipPlayer.addStopAction { mutex.decrement(1) }
            singleClipPlayer
        }
        mutex.block()
    }

    fun playInBackground(uniClipPlayerProvider: () -> UniClipPlayer) {
        playUsing(uniClipPlayerProvider)
    }
}