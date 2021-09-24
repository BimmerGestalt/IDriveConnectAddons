package io.bimmergestalt.idriveconnectaddons.cdsdetails

import android.app.Application
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.AndroidViewModel
import io.bimmergestalt.idriveconnectaddons.lib.CDSLiveData
import io.bimmergestalt.idriveconnectaddons.lib.CDSProperty
import me.tatarka.bindingcollectionadapter2.ItemBinding

class MainModel(app: Application): AndroidViewModel(app) {
    val datapoints = ObservableArrayList<Pair<String, CDSLiveData>>().apply {
        addAll(CDSProperty.values().map {
            Pair(it.propertyName.replace(".", "\u200B."), CDSLiveData(app.applicationContext, it))
        })
    }
    val cdsItemBinding: ItemBinding<Pair<String, CDSLiveData>> = ItemBinding.of(BR.item, R.layout.item_cds)
}