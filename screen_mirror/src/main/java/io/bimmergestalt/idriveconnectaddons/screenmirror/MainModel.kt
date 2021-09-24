package io.bimmergestalt.idriveconnectaddons.screenmirror

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import io.bimmergestalt.idriveconnectaddons.lib.LiveDataHelpers.map

class MainModel: ViewModel() {
    val mirroringState = ScreenMirrorProvider.state
    val mirroringStateText: LiveData<Context.() -> String> = ScreenMirrorProvider.state.map({getString(R.string.lbl_status_not_ready)}) {
        when (it) {
            MirroringState.NOT_ALLOWED -> {
                { getString(R.string.lbl_status_not_allowed) }
            }
            MirroringState.WAITING -> {
                { getString(R.string.lbl_status_waiting) }
            }
            MirroringState.ACTIVE -> {
                { getString(R.string.lbl_status_active) }
            }
            else -> {
                { getString(R.string.lbl_status_not_ready) }
            }
        }
    }
}