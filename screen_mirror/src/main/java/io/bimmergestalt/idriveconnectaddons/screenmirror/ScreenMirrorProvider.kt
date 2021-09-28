package io.bimmergestalt.idriveconnectaddons.screenmirror

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageFormat
import android.graphics.PixelFormat
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.Image
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.os.Handler
import android.util.Log
import androidx.lifecycle.MutableLiveData
import java.io.ByteArrayOutputStream

enum class MirroringState {
    NOT_ALLOWED,
    WAITING,
    ACTIVE
}
class ScreenMirrorProvider(val handler: Handler) {
    companion object {
        /** Projection is provided by the RequestActivity after fetching permission */
        var projection: MediaProjection? = null
            set(value) {
                field = value;
                state.postValue(if (field == null) MirroringState.NOT_ALLOWED else MirroringState.WAITING)
            }

        /** The current state to display in the phone UI */
        var state = MutableLiveData(MirroringState.NOT_ALLOWED)
    }

    private var size: Pair<Int, Int> = 1280 to 480
    private var lastFrameTime = 0L
    private var frameTime = 0       // minimum time each frame should be displayed

    /** The surface that VirtualDisplay will render to */
    private var imageReader: ImageReader? = null

    /** The VirtualDisplay that represents the mirror
     * We need to detach and reattach imageReader to pause mirroring
     */
    private var display: VirtualDisplay? = null

    /** Reused objects to reduce GC load for each frame */
    private var bmp: Bitmap? = null
    private val jpg = ByteArrayOutputStream()

    // will be set while the car app is visible, ready to display images
    var callback: ((ByteArray) -> Unit)? = null

    /**
     * Sets the virtual screen size, must be done before starting
     */
    fun setSize(width: Int, height: Int) {
        size = width to height
    }

    fun setFrameTime(time: Int) {
        frameTime = time
    }

    fun start() {
        createImageReader()
        schedulePoll()
    }

    fun pause() {
        display?.surface = null
        handler.removeCallbacks(poll)
        state.postValue(if (projection == null) MirroringState.NOT_ALLOWED else MirroringState.WAITING)
    }

    fun stop() {
        pause()
        imageReader?.close()
        imageReader = null
        display?.release()
        display = null
        projection?.stop()
        projection = null
    }

    val poll = Runnable {
        createImageReader()
        fetchImage()
        schedulePoll()
    }
    fun schedulePoll(time: Long = 1000L) {
        if (callback != null) {
            handler.removeCallbacks(poll)
            handler.postDelayed(poll, time)
        }
    }

    /**
     * Create the ImageReader if it is missing
     * This might be the case if we don't yet have permission when we start()
     * and so it is called during the poll loop to check until the user grants permission
     */
    @SuppressLint("WrongConstant")
    fun createImageReader() {
        if (projection != null && imageReader == null) {
            val imageReader = ImageReader.newInstance(size.first, size.second, PixelFormat.RGBA_8888, 2)
            imageReader.setOnImageAvailableListener(imageListener, handler)
            val flags = DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR or DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC or DisplayManager.VIRTUAL_DISPLAY_FLAG_PRESENTATION
            val display = try {
                projection?.createVirtualDisplay("idrive-screen-mirror", imageReader.width, imageReader.height, 200, flags, imageReader.surface, null, handler)
            } catch (e: Exception) {
                Log.w(TAG, "Failed to create mirror display", e)
                null
            }
            if (display != null) {
                this.imageReader = imageReader
                this.display = display
                state.postValue(MirroringState.ACTIVE)
            } else {
                projection = null       // didn't work, request a new Projection
            }
        } else if (projection != null && imageReader != null) {
            // resuming a previous pause
            this.display?.surface = imageReader?.surface
            state.postValue(MirroringState.ACTIVE)
        }
    }

    /**
     * When notified by the Projection that we have a new frame,
     * schedule a poll on the Handler to work on this frame
     * to let any currently-running frame processing finish
     */
    val imageListener = ImageReader.OnImageAvailableListener {
        fetchImage()
        schedulePoll()
    }

    fun fetchImage() {
        // enforce the minimum frame time
        val frameDelay = frameTime - (System.currentTimeMillis() - lastFrameTime)
        if (frameDelay > 0) {
            schedulePoll(frameDelay)
            return
        }

        val image = imageReader?.acquireLatestImage()
        if (image != null) {
            lastFrameTime = System.currentTimeMillis()
            val jpegData = compressImage(image)
            this.callback?.invoke(jpegData)
            image.close()
        }
    }

    /** Determine the proper Bitmap Config to match the screen format */
    private fun getBitmapConfig(imageFormat: Int): Bitmap.Config {
        return when (imageFormat) {
            ImageFormat.RGB_565 -> Bitmap.Config.RGB_565
            ImageFormat.FLEX_RGBA_8888 -> Bitmap.Config.ARGB_8888
            1 -> Bitmap.Config.ARGB_8888
            ImageFormat.FLEX_RGB_888 -> Bitmap.Config.ARGB_8888
            else -> Bitmap.Config.ARGB_8888
        }
    }

    private fun compressImage(image: Image): ByteArray {
        val planes = image.planes
        val buffer = planes[0].buffer
        val padding = planes[0].rowStride - planes[0].pixelStride * image.width
        val width = image.width + padding / planes[0].pixelStride
        var bmp = bmp ?: Bitmap.createBitmap(width, image.height, getBitmapConfig(image.format))
        if (bmp == null || bmp.width != width || bmp.height != image.height) {
            bmp = Bitmap.createBitmap(width, image.height, getBitmapConfig(image.format))
        }
        bmp.copyPixelsFromBuffer(buffer)

        // wish that ByteArrayOutputBuffer wouldn't clone the array in `toByteArray`
        // but it seems that Java doesn't support array slices
        jpg.reset()
        bmp.compress(Bitmap.CompressFormat.JPEG, 65, jpg)
        return jpg.toByteArray()
    }
}