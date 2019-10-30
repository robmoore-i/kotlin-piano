package com.zuhlke.rob.sample

internal class FullClipPlayer : BaseClipPlayer() {
    override fun onClipStop() {
        complete()
    }
}
