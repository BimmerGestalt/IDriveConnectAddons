package io.bimmergestalt.idriveconnectaddons.screenmirror

import android.content.Context

interface ScreenMirrorInteraction {
    fun click()
    fun moveUp()
    fun moveDown()
}

class MirroringAccessibilityInteraction(val context: Context): ScreenMirrorInteraction {
    override fun click() {
        MirroringAccessibilityService.sendKey(context, SendKey.CLICK)
    }
    override fun moveUp() {
        MirroringAccessibilityService.sendKey(context, SendKey.FOCUS_BACKWARD)
    }
    override fun moveDown() {
        MirroringAccessibilityService.sendKey(context, SendKey.FOCUS_FORWARD)
    }
}