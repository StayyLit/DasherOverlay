package com.example.dashero.overlay

import kotlinx.coroutines.flow.first
import android.os.Build
import com.example.dashero.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.Gravity
import android.view.WindowManager
import androidx.compose.ui.platform.ComposeView
import androidx.core.app.NotificationCompat
import com.example.dashero.data.SettingsRepo
import kotlinx.coroutines.*

class OverlayService : Service() {

    private lateinit var wm: WindowManager
    private var overlayView: ComposeView? = null
    private lateinit var params: WindowManager.LayoutParams
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()
        wm = getSystemService(WINDOW_SERVICE) as WindowManager
        startForeground(1001, buildNotif())

        params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            x = 24
            y = 200
        }

        val repo = SettingsRepo(applicationContext)

        overlayView = ComposeView(this).apply {
            setContent {
                OverlayRoot(
                    repo = repo,
                    initialX = params.x,
                    initialY = params.y,
                    onMove = { x, y ->
                        params.x = x
                        params.y = y
                        try { wm.updateViewLayout(this, params) } catch (_: Throwable) {}
                        scope.launch(Dispatchers.IO) { repo.saveOverlayPos(x, y) }
                    }
                )
            }
        }

        // restore saved position
        scope.launch(Dispatchers.IO) {
            val pos = repo.overlayPosFlow().first()
            withContext(Dispatchers.Main) {
                params.x = pos.first
                params.y = pos.second
                overlayView?.let { wm.addView(it, params) }
            }
        }
    }

    override fun onDestroy() {
        overlayView?.let { try { wm.removeView(it) } catch (_: Throwable) {} }
        overlayView = null
        scope.cancel()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun buildNotif(): Notification {
        val channelId = "overlay_channel"
        if (Build.VERSION.SDK_INT >= 26) {
            val nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            nm.createNotificationChannel(
                NotificationChannel(channelId, "Overlay", NotificationManager.IMPORTANCE_LOW)
            )
        }
        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Dasher Overlay running")
            .setContentText("Showing $/mile and $/hour on top of offers")
            .setSmallIcon(R.mipmap.ic_launcher)
            .build()
    }
}
