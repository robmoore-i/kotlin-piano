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
}

class MultiClipPlayer(private val uniClipPlayerProvider: () -> UniClipPlayer) : ClipPlayer<MultiClip, Semaphore> {
    override fun play(clip: MultiClip, mutex: Semaphore) {
        mutex.increment(clip.cardinality())
        clip.playUsing {
            val singleClipPlayer = uniClipPlayerProvider.invoke()
            singleClipPlayer.addStopAction { mutex.decrement(1) }
            singleClipPlayer
        }
        mutex.block()
    }

    override fun playInBackground(clip: MultiClip) {
        clip.playUsing(uniClipPlayerProvider)
    }
}