package com.zuhlke.rob

interface MultiClip : Clip<() -> UniClipPlayer>

class MultiAudioClip(private vararg val subclips: UniClip) : MultiClip {
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