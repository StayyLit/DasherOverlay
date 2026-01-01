package com.example.dashero.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.dashero.data.SettingsRepo
import com.example.dashero.overlay.OverlayService

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
  repo: SettingsRepo,
  onOpenSettings: () -> Unit
) {
  val ctx = LocalContext.current

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
      Button(
        onClick = { requestOverlayPermission(ctx) },
        modifier = Modifier.fillMaxWidth()
      ) { Text("Open Overlay Permission") }

      Text("2) Enable Accessibility service (Offer Reading)")
      Button(
        onClick = { openAccessibilitySettings(ctx) },
        modifier = Modifier.fillMaxWidth()
      ) { Text("Open Accessibility Settings") }

      Divider()

      Text("3) Start / Stop Overlay")
      Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        Button(onClick = { startOverlay(ctx) }) { Text("Start") }
        OutlinedButton(onClick = { stopOverlay(ctx) }) { Text("Stop") }
      }

      Divider()
      Text("Tip: Drag the overlay to position it. Position is saved.")
      Spacer(modifier = Modifier.height(8.dp))
    }
  }
}

private fun requestOverlayPermission(ctx: Context) {
  val intent = Intent(
    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
    Uri.parse("package:${ctx.packageName}")
  ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
  ctx.startActivity(intent)
}

private fun openAccessibilitySettings(ctx: Context) {
  val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
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
