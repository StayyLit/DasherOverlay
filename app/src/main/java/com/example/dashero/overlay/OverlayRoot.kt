package com.example.dashero.overlay

import androidx.compose.runtime.*
import com.example.dashero.data.SettingsRepo
import com.example.dashero.data.UserSettings
import kotlinx.coroutines.flow.collectLatest

@Composable
fun OverlayRoot(
    repo: SettingsRepo,
    initialX: Int,
    initialY: Int,
    onMove: (Int, Int) -> Unit
) {
    var settings by remember { mutableStateOf(UserSettings()) }

    LaunchedEffect(Unit) {
        repo.settingsFlow.collectLatest { settings = it }
    }

    OverlayComposable(
        settings = settings,
        startX = initialX,
        startY = initialY,
        onMove = onMove
    )
}
