package io.bimmergestalt.idriveconnectaddons.screenmirror

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

object L {
    var MIRRORING_TITLE = "Screen Mirroring"
    var PERMISSION_PROMPT = "Please open the phone app to grant screen recording permission"

    fun loadResources(context: Context, locale: Locale? = null) {
        val thisContext = if (locale == null) { context } else {
            val origConf = context.resources.configuration
            val localeConf = Configuration(origConf)
            localeConf.setLocale(locale)
            context.createConfigurationContext(localeConf)
        }

        MIRRORING_TITLE = thisContext.getString(R.string.MIRRORING_TITLE)
        PERMISSION_PROMPT = thisContext.getString(R.string.PERMISSION_PROMPT)
    }
}