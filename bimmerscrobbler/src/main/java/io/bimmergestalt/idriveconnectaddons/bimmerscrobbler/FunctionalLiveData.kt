package io.bimmergestalt.idriveconnectaddons.bimmerscrobbler

import androidx.lifecycle.LiveData

/**
 * A LiveData that sets its value to the producer output whenever it becomes active
 */
class FunctionalLiveData<T>(val producer: () -> T?): LiveData<T>() {
	override fun onActive() {
		super.onActive()
		value = producer()
	}
}