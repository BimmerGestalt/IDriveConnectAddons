package io.bimmergestalt.idriveconnectaddons.screenmirror.carapp.views

import io.bimmergestalt.idriveconnectaddons.screenmirror.L
import io.bimmergestalt.idriveconnectaddons.screenmirror.ScreenMirrorProvider
import io.bimmergestalt.idriveconnectkit.rhmi.*

class ImageState(val state: RHMIState, val screenMirrorProvider: ScreenMirrorProvider) {
    companion object {
        fun fits(state: RHMIState): Boolean {
            return state is RHMIState.PlainState &&
                    state.componentsList.filterIsInstance<RHMIComponent.Image>().any {
                        it.getModel() is RHMIModel.RaImageModel
                    } &&
                    state.componentsList.filterIsInstance<RHMIComponent.List>().isNotEmpty()
        }
    }

    val image = state.componentsList.filterIsInstance<RHMIComponent.Image>().first {
        it.getModel() is RHMIModel.RaImageModel
    }
    val imageModel = image.getModel()?.asRaImageModel()!!
    val infoList = state.componentsList.filterIsInstance<RHMIComponent.List>().first()

    fun initWidgets() {
        state.setProperty(RHMIProperty.PropertyId.HMISTATE_TABLETYPE, 3)
        state.setProperty(RHMIProperty.PropertyId.HMISTATE_TABLELAYOUT, "1,0,7")
        state.getTextModel()?.asRaDataModel()?.value = L.MIRRORING_TITLE
        image.setProperty(RHMIProperty.PropertyId.WIDTH, 1440)
        image.setProperty(RHMIProperty.PropertyId.HEIGHT, 540)
        infoList.setProperty(RHMIProperty.PropertyId.LIST_COLUMNWIDTH, "*")
        infoList.getModel()?.value = RHMIModel.RaListModel.RHMIListConcrete(1).also {
            it.addRow(arrayOf("${L.PERMISSION_PROMPT}\n"))
        }
        showPermissionPrompt()

        state.focusCallback = FocusCallback { focused ->
            if (focused) {
                screenMirrorProvider.callback = {
                    imageModel.value = it

                    // the permission is working! hide the permission prompt
                    showImage()
                }
                screenMirrorProvider.start()
            } else {
                screenMirrorProvider.callback = null
                screenMirrorProvider.pause()
            }
        }
    }

    /** Toggles widget visibility to hide the permission prompt and show the image
     *  Relies on idempotency to avoid property calls to the same state
     */
    private fun showImage() {
        infoList.setVisible(false)
        image.setVisible(true)
    }

    /** Toggles widget visibility to hide the image and show the permission prompt
     *  Relies on idempotency to avoid property calls to the same state
     */
    private fun showPermissionPrompt() {
        image.setVisible(false)
        infoList.setVisible(true)
    }
}