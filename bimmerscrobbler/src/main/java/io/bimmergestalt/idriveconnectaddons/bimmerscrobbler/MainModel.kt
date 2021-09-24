package io.bimmergestalt.idriveconnectaddons.bimmerscrobbler

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

object MainModel: ViewModel() {
    val isConnected = MutableLiveData(false)
    val source = MutableLiveData("")
    val artist = MutableLiveData("")
    val album = MutableLiveData("")
    val title = MutableLiveData("")
}