package io.bimmergestalt.idriveconnectaddons.screenmirror

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Point
import android.graphics.Rect
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.lang.IllegalArgumentException

enum class SendKey {
    CLICK,
    FOCUS_BACKWARD,
    FOCUS_FORWARD,
}

class MirroringAccessibilityService: AccessibilityService() {
    companion object {
        val ACTION_SENDKEY = "io.bimmergestalt.idriveconnectaddons.screenmirror.ACTION_SENDKEY"
        val ACTION_HIGHLIGHT = "io.bimmergestalt.idriveconnectaddons.screenmirror.ACTION_HIGHLIGHT"
        val EXTRA_KEY = "key"
        val EXTRA_RECT = "rect"

        // UI status
        private val _connected = MutableLiveData(false)
        val connected: LiveData<Boolean> = _connected

        // convenient sendKey from other objects
        fun sendKey(context: Context, key: SendKey) {
            Log.i(TAG, "Sending remote SendKey of $key")
            val intent = Intent(context, MirroringAccessibilityService::class.java)
                .setAction(ACTION_SENDKEY)
                .putExtra(EXTRA_KEY, key.name)
            context.startService(intent)
        }
    }

    private var connected = false
    private val highlightRect = Rect()      // outRect for getBoundsInScreen
    private val screenSize = Point()        // outPoint for getRealSize

    override fun onServiceConnected() {
        super.onServiceConnected()
        connected = true
        _connected.value = true

        applicationContext.getSystemService(WindowManager::class.java).defaultDisplay.getRealSize(screenSize)
        Log.i(TAG, "getRealSize provides a screen size of ${screenSize.x}x${screenSize.y}")

        val displayMetrics = DisplayMetrics()
        applicationContext.getSystemService(WindowManager::class.java).defaultDisplay.getRealMetrics(displayMetrics)
        Log.i(TAG, "getRealMetrics provides a screen size of ${displayMetrics.widthPixels}x${displayMetrics.heightPixels}")

        val systemMetrics = Resources.getSystem().displayMetrics
        Log.i(TAG, "system.displayMetrics provides a screen size of ${systemMetrics.widthPixels}x${systemMetrics.heightPixels}")
    }

    override fun onDestroy() {
        super.onDestroy()
        _connected.value = false
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (connected && intent?.action == ACTION_SENDKEY && intent.hasExtra(EXTRA_KEY)) {
            val sendKeyParam = try {
                SendKey.valueOf(intent.getStringExtra(EXTRA_KEY) ?: "")
            } catch (e: IllegalArgumentException) { null }
            Log.i(TAG, "Received remote SendKey of $sendKeyParam")
            if (sendKeyParam != null) {
                sendKey(sendKeyParam)
            }
        }
        return START_NOT_STICKY
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        Log.i(TAG, "Received accessibility event ${event?.eventType}: ${event?.contentDescription}")
    }

    private fun sendKey(key: SendKey) {
        val focused: AccessibilityNodeInfo? = rootInActiveWindow?.findFocus(AccessibilityNodeInfo.FOCUS_ACCESSIBILITY) ?:
            rootInActiveWindow?.findFocus(AccessibilityNodeInfo.FOCUS_INPUT)
        if (key == SendKey.CLICK) {
            focused?.performAction(AccessibilityNodeInfo.ACTION_CLICK)
            rootInActiveWindow?.performAction(AccessibilityNodeInfo.ACTION_CLEAR_FOCUS)
            rootInActiveWindow?.performAction(AccessibilityNodeInfo.ACTION_CLEAR_ACCESSIBILITY_FOCUS)
            announceHighlightRect(null)
        } else if (key == SendKey.FOCUS_BACKWARD || key == SendKey.FOCUS_FORWARD) {
            // change focus instead
            val newFocus = if (focused != null) {
                when (key) {
                    SendKey.FOCUS_BACKWARD -> focusSearchClickable(focused, View.FOCUS_BACKWARD)
                    SendKey.FOCUS_FORWARD -> focusSearchClickable(focused, View.FOCUS_FORWARD)
                    else -> null
                }
            } else {
                focusSearchClickable(rootInActiveWindow, View.FOCUS_FORWARD)
            }
            if (newFocus != null) {
                newFocus.performAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SHOW_ON_SCREEN.id)
                newFocus.performAction(AccessibilityNodeInfo.ACTION_FOCUS)
                newFocus.performAction(AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS)
                announceHighlightRect(newFocus)
            }
        }
    }

    private fun focusSearchClickable(start: AccessibilityNodeInfo?, direction: Int): AccessibilityNodeInfo? {
        var count = 0
        var newFocus: AccessibilityNodeInfo? = start?.focusSearch(direction)
        while (newFocus != null && count < 100 && !(newFocus.isClickable && newFocus != start)) {
            newFocus = newFocus.focusSearch(direction)
            count += 1
        }
        return newFocus
    }

    private fun announceHighlightRect(focusedNode: AccessibilityNodeInfo?) {
        if (focusedNode != null) {
            focusedNode.getBoundsInScreen(highlightRect)
            val intent = Intent(ACTION_HIGHLIGHT)
                .setPackage(BuildConfig.APPLICATION_ID)
                .putExtra(EXTRA_RECT, highlightRect)
            sendBroadcast(intent)
        } else {
            val intent = Intent(ACTION_HIGHLIGHT)
                .setPackage(BuildConfig.APPLICATION_ID)
            sendBroadcast(intent)
        }
    }

    override fun onInterrupt() {
    }
}