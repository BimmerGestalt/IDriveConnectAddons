package io.bimmergestalt.idriveconnectaddons.cdsgauge

import android.content.Context
import androidx.databinding.BindingAdapter
import com.github.anastr.speedviewlib.Gauge

object DataBindingHelpers {
    // Dynamic text
    @JvmStatic
    @BindingAdapter("sv_unit")
    fun setUnit(view: Gauge, value: Context.() -> String) {
        view.unit = view.context.run(value)
    }
    @JvmStatic
    @BindingAdapter("sv_speed")
    fun setSpeed(view: Gauge, value: Double) {
        view.speedTo(value.toFloat(), 500)
    }
}