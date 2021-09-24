package io.bimmergestalt.idriveconnectaddons.bimmerscrobbler

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class InstalledScrobblersViewModel(app: Application): AndroidViewModel(app) {

	val hasSLS = FunctionalLiveData {
		ScrobbleAnnouncer(app.applicationContext).hasSLS
	}
}