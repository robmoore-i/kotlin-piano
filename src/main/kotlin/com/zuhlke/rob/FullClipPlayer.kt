package com.zuhlke.rob

internal class FullClipPlayer : BaseClipPlayer() {
    override fun onClipStop() {
        complete()
    }
}
