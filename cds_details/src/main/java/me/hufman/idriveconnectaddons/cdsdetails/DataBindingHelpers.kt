package me.hufman.idriveconnectaddons.cdsdetails

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.gson.GsonBuilder
import me.hufman.idriveconnectaddons.lib.CDSLiveData


object DataBindingHelpers {
    val gsonPrinter = GsonBuilder().setPrettyPrinting().create()

    /**
     * Format a CDSLiveData to a pretty-printed JSON string
     */
    @JvmStatic
    @BindingAdapter("android:text")
    fun setImageViewResource(view: TextView, liveData: CDSLiveData) {
        val current = liveData.value
        if (current != null) {
            view.text = gsonPrinter.toJson(liveData.value).replace("{\n  \"", "{ \"")
        } else {
            view.text = ""
        }
    }
}

