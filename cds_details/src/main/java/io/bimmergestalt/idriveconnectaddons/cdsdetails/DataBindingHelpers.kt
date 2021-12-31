package io.bimmergestalt.idriveconnectaddons.cdsdetails

import android.widget.TextView
import androidx.databinding.BindingAdapter
import io.bimmergestalt.idriveconnectaddons.lib.CDSLiveData


object DataBindingHelpers {
    /**
     * Format a CDSLiveData to a pretty-printed JSON string
     */
    @JvmStatic
    @BindingAdapter("android:text")
    fun setText(view: TextView, liveData: CDSLiveData) {
        val current = liveData.value
        if (current != null) {
            view.text = current.toString()
        } else {
            view.text = ""
        }
    }
}

