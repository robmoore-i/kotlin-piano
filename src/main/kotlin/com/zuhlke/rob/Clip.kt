package com.zuhlke.rob

interface Clip<Player> {
    fun cardinality(): Int

    fun playUsing(player: Player)
    fun stop()
    fun isComplete(): Boolean
}