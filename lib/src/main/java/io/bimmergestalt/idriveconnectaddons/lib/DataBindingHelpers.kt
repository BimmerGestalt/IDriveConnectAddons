package io.bimmergestalt.idriveconnectaddons.lib

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter

object DataBindingHelpers {

    /** Toggle a View's visibility by a boolean */
    var View.visible: Boolean
        get() {
            return this.visibility == View.VISIBLE
        }
        set(value) {
            this.visibility = if (value) View.VISIBLE else View.GONE
        }

    @JvmStatic
    @BindingAdapter("android:visibility")
    fun setViewVisibility(view: View, visible: Boolean) {
        view.visible = visible
    }

    // Dynamic text
    @JvmStatic
    @BindingAdapter("android:text")
    fun setText(view: TextView, value: Context.() -> String) {
        view.text = view.context.run(value)
    }

    // Dynamic text
    @JvmStatic
    @BindingAdapter("android:visibility")
    fun setVisibilityByTextGetter(view: View, value: Context.() -> String) {
        val text = view.context.run(value)
        view.visible = text.isNotBlank()
    }
}
