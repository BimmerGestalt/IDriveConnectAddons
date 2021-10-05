package io.bimmergestalt.idriveconnectaddons.screenmirror

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.*
import android.view.WindowManager
import kotlin.math.max

class FocusRectDecorator(val context: Context): BroadcastReceiver() {
    companion object {
        const val DECAY = 8000L
    }
    var lastReceivedTime = 0L
    val currentCanvasSize = Point(100, 100)
    val previousScreenSize = Point(100, 100)
    val currentScreenSize = Point(100, 100)
    val insetCanvasSize = Rect()
    var rect: Rect? = null
    val paint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 3f
        color = Color.YELLOW
        alpha = 255
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == MirroringAccessibilityService.ACTION_HIGHLIGHT) {
            calculateInsetSize()        // make sure this is updated

            lastReceivedTime = System.currentTimeMillis()
            rect = intent.getParcelableExtra(MirroringAccessibilityService.EXTRA_RECT)

            val rect = rect
            if (rect != null) {
                scaleRect(rect)
            }
        }
    }

    fun register() {
        context.registerReceiver(this, IntentFilter(MirroringAccessibilityService.ACTION_HIGHLIGHT))
    }

    fun unregister() {
        context.unregisterReceiver(this)
    }

    fun calculateInsetSize() {
        context.getSystemService(WindowManager::class.java).defaultDisplay.getRealSize(currentScreenSize)
        if (previousScreenSize != currentScreenSize) {
            // from https://stackoverflow.com/a/21960701/169035
            val innerAspectRatio = currentScreenSize.x.toFloat() / currentScreenSize.y      // the rect to squish
            val outerAspectRatio = currentCanvasSize.x.toFloat() / currentCanvasSize.y      // the outer bounds rect

            val resizeFactor = if (innerAspectRatio >= outerAspectRatio) {
                currentCanvasSize.x.toFloat() / currentScreenSize.x
            } else {
                currentCanvasSize.y.toFloat() / currentScreenSize.y
            }

            val newWidth = currentScreenSize.x * resizeFactor
            val newHeight = currentScreenSize.y * resizeFactor
            val newLeft = (currentCanvasSize.x - newWidth) / 2f
            val newTop = (currentCanvasSize.y - newHeight) / 2f

            insetCanvasSize.left = newLeft.toInt()
            insetCanvasSize.top = newTop.toInt()
            insetCanvasSize.right = insetCanvasSize.left + newWidth.toInt()
            insetCanvasSize.bottom = insetCanvasSize.top + newHeight.toInt()

            // remember so we don't need to do it again
            previousScreenSize.set(currentScreenSize.x, currentScreenSize.y)
        }
    }

    fun scaleRect(newRect: Rect) {
        val xRatio = insetCanvasSize.width().toFloat() / currentScreenSize.x
        val yRatio = insetCanvasSize.height().toFloat() / currentScreenSize.y

        newRect.left = (newRect.left * xRatio).toInt() + insetCanvasSize.left
        newRect.right = (newRect.right * xRatio).toInt() + insetCanvasSize.left
        newRect.top = (newRect.top * yRatio).toInt() + insetCanvasSize.top
        newRect.bottom = (newRect.bottom * yRatio).toInt() + insetCanvasSize.top
    }

    fun decorate(canvas: Canvas) {
        if (canvas.width != currentCanvasSize.x || canvas.height != currentCanvasSize.y) {
            currentCanvasSize.x = canvas.width
            currentCanvasSize.y = canvas.height
            rect = null
        }
        val rect = rect
        if (rect != null) {
            val colorDecay: Float = max(0f, 1 - (System.currentTimeMillis() - lastReceivedTime) / DECAY.toFloat())
            paint.alpha = (255 * colorDecay).toInt()

            canvas.drawRect(rect, paint)
        }
    }
}