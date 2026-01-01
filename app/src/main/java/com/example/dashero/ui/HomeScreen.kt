package com.example.dashero.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.dashero.data.SettingsRepo
import com.example.dashero.overlay.OverlayService

@Composable
fun HomeScreen(
    repo: SettingsRepo,
    onOpenSettings: () -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dasher Overlay") },
                actions = {
                    TextButton(onClick = onOpenSettings) { Text("Settings") }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("1) Enable Overlay permission")
            Button(onClick = { requestOverlayPermission(context) }) {
                Text("Open Overlay Permission")
            }

            Text("2) Enable Accessibility service (Offer Reading)")
            Button(onClick = { openAccessibilitySettings(context) }) {
                Text("Open Accessibility Settings")
            }

            Divider()

            Text("3) Start / Stop Overlay")
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(onClick = { startOverlay(context) }) { Text("Start") }
                OutlinedButton(onClick = { stopOverlay(context) }) { Text("Stop") }
            }

            Divider()
            Text("Tip: Drag the overlay to position it. Position is saved.")
        }
    }
}

private fun requestOverlayPermission(ctx: Context) {
    val intent = Intent(
        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
        Uri.parse("package:${ctx.packageName}")
    )
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    ctx.startActivity(intent)
}

private fun openAccessibilitySettings(ctx: Context) {
    val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    ctx.startActivity(intent)
}

private fun startOverlay(ctx: Context) {
    val i = Intent(ctx, OverlayService::class.java)
    ContextCompat.startForegroundService(ctx, i)
}

private fun stopOverlay(ctx: Context) {
    val i = Intent(ctx, OverlayService::class.java)
    ctx.stopService(i)
}
