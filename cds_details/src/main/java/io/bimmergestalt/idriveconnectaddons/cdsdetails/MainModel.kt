package io.bimmergestalt.idriveconnectaddons.cdsdetails

import android.app.Application
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.AndroidViewModel
import io.bimmergestalt.idriveconnectkit.CDSProperty
import io.bimmergestalt.idriveconnectkit.android.CDSLiveData
import io.bimmergestalt.idriveconnectaddons.lib.CDSLiveData
import me.tatarka.bindingcollectionadapter2.ItemBinding

class MainModel(app: Application): AndroidViewModel(app) {
    val datapoints = ObservableArrayList<Pair<String, CDSLiveData>>().apply {
        addAll(CDSProperty.values().map {
            Pair(it.propertyName.replace(".", "\u200B."), CDSLiveData(app.applicationContext.contentResolver, it))
        })
    }
    val cdsItemBinding: ItemBinding<Pair<String, CDSLiveData>> = ItemBinding.of(BR.item, R.layout.item_cds)
}