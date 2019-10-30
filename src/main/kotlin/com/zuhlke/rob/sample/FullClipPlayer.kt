package com.zuhlke.rob.sample

internal class FullClipPlayer : BaseClipPlayer(PlaybackLock()) {
    override fun onClipStop() {
        complete()
    }
}
