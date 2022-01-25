package io.bimmergestalt.idriveconnectaddons.lib

import android.content.ContentResolver
import android.content.Context
import io.bimmergestalt.idriveconnectkit.CDSProperty
import io.bimmergestalt.idriveconnectkit.android.CDSLiveData
import io.bimmergestalt.idriveconnectkit.android.CDSLiveDataProvider

fun CDSLiveData(context: Context, property: CDSProperty): CDSLiveData {
    return CDSLiveData(context.contentResolver, property.ident)
}
fun CDSLiveData(contentResolver: ContentResolver, property: CDSProperty): CDSLiveData {
    return CDSLiveData(contentResolver, property.ident)
}

object CDSLiveDataProvider {
    operator fun CDSLiveDataProvider.get(property: CDSProperty): CDSLiveData {
        return this[property.ident]
    }
}