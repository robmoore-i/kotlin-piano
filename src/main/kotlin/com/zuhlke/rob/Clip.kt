package com.zuhlke.rob

interface Clip<Player> {
    fun cardinality(): Int

    fun playUsing(player: Player)
    fun stop()
    fun isComplete(): Boolean
}

interface UClip : Clip<UniClipPlayer> {
    override fun cardinality() = 1
}

interface MClip : Clip<() -> UniClipPlayer> {
}