package me.hufman.idriveconnectaddons.androbd_gestalt

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

object MainModel: ViewModel() {
    val isGestaltConnected = MutableLiveData(false)
    val isAndrobdConnected = MutableLiveData(false)
}