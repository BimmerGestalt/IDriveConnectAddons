package io.bimmergestalt.idriveconnectaddons.screenmirror.carapp.views

import io.bimmergestalt.idriveconnectaddons.screenmirror.L
import io.bimmergestalt.idriveconnectaddons.screenmirror.ScreenMirrorInteraction
import io.bimmergestalt.idriveconnectaddons.screenmirror.ScreenMirrorProvider
import io.bimmergestalt.idriveconnectkit.rhmi.*

class ImageState(val state: RHMIState, val screenMirrorProvider: ScreenMirrorProvider, val interaction: ScreenMirrorInteraction) {
    companion object {
        fun fits(state: RHMIState): Boolean {
            return state is RHMIState.PlainState &&
                    state.componentsList.filterIsInstance<RHMIComponent.Image>().any {
                        it.getModel() is RHMIModel.RaImageModel
                    } &&
                    state.componentsList.filterIsInstance<RHMIComponent.Label>().isNotEmpty() &&
                    state.componentsList.filterIsInstance<RHMIComponent.List>().isNotEmpty()
        }
    }

    val image = state.componentsList.filterIsInstance<RHMIComponent.Image>().first {
        it.getModel() is RHMIModel.RaImageModel
    }
    val imageModel = image.getModel()?.asRaImageModel()!!
    val infoLabel = state.componentsList.filterIsInstance<RHMIComponent.Label>().first()
    val inputList = state.componentsList.filterIsInstance<RHMIComponent.List>().first()
    val inputFocusEvent = state.app.events.values.filterIsInstance<RHMIEvent.FocusEvent>().first()

    fun initWidgets() {
        state.setProperty(RHMIProperty.PropertyId.HMISTATE_TABLETYPE, 3)
        state.setProperty(RHMIProperty.PropertyId.HMISTATE_TABLELAYOUT, "1,0,7")
        state.getTextModel()?.asRaDataModel()?.value = L.MIRRORING_TITLE
        image.setProperty(RHMIProperty.PropertyId.WIDTH, 1440)
        image.setProperty(RHMIProperty.PropertyId.HEIGHT, 540)
        infoLabel.getModel()?.asRaDataModel()?.value = L.PERMISSION_PROMPT + "\n"
        infoLabel.setProperty(RHMIProperty.PropertyId.POSITION_X, 0)
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

        // prepare the input list
        val inputListData = RHMIModel.RaListModel.RHMIListConcrete(1)
        repeat(3) { inputListData.addRow(arrayOf("<")) }  // previous
        inputListData.addRow(arrayOf(L.MIRRORING_TITLE)) // click
        repeat(3) { inputListData.addRow(arrayOf(">")) }  // next
        inputList.getModel()?.asRaListModel()?.setValue(inputListData, 0, inputListData.height, inputListData.height)
        inputFocusEvent.triggerEvent(mapOf(0 to inputList.id, 41 to 3))

        inputList.setProperty(RHMIProperty.PropertyId.POSITION_X.id, -50000)  // positionX, so that we don't see it but should still be interacting with it
        inputList.setProperty(RHMIProperty.PropertyId.POSITION_Y.id, 0)  // positionY, so that we don't see it but should still be interacting with it
        inputList.setProperty(RHMIProperty.PropertyId.BOOKMARKABLE, true)   // allow for bookmarking inside this view
        inputList.setProperty(RHMIProperty.PropertyId.LIST_COLUMNWIDTH, "100")
        inputList.setVisible(true)

        // handle bookmark click
        inputList.getAction()?.asRAAction()?.rhmiActionCallback = object: RHMIActionListCallback {
            override fun onAction(index: Int, invokedBy: Int?) {
                if (invokedBy == 2) {
                    inputFocusEvent.triggerEvent(mapOf(0 to state.id))
                } else {
                    interaction.click()
                }
            }
        }
        // handle scroll
        inputList.getSelectAction()?.asRAAction()?.rhmiActionCallback = RHMIActionListCallback { listIndex ->
            // each wheel click through the list will trigger another step of 1
            val steps = kotlin.math.abs(3 - listIndex)
            // trigger the interaction
            when (listIndex) {
                in 0..2 -> interaction.moveUp()
                in 4..6 -> interaction.moveDown()
                else -> null        // was reset to the start of the list
            }
            // reset focus to the middle of the list, if it was moved
            if (steps > 0) {
                inputFocusEvent.triggerEvent(mapOf(0 to inputList.id, 41 to 3))
            }
        }
    }

    /** Toggles widget visibility to hide the permission prompt and show the image
     *  Relies on idempotency to avoid property calls to the same state
     */
    private fun showImage() {
        infoLabel.setVisible(false)
        image.setVisible(true)
    }

    /** Toggles widget visibility to hide the image and show the permission prompt
     *  Relies on idempotency to avoid property calls to the same state
     */
    private fun showPermissionPrompt() {
        image.setVisible(false)
        infoLabel.setVisible(true)
    }
}