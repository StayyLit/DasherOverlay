package com.example.dashero.overlay

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dashero.calc.MetricsCalculator
import com.example.dashero.data.UserSettings
import kotlin.math.roundToInt

@Composable
fun OverlayComposable(
    settings: UserSettings,
    startX: Int,
    startY: Int,
    onMove: (Int, Int) -> Unit
) {
    val offer by OverlayServiceBridge.offerFlow.collectAsState()
    val metrics = offer?.let {
        MetricsCalculator.compute(
            offer = it,
            baseMinutes = settings.baseMinutes,
            minutesPerMile = settings.minutesPerMile,
            avgWaitMinutes = settings.avgWaitMinutes
        )
    }

    // Color rules (don’t hardcode colors; we’ll use theme + borders as signals)
    val dpmOk = (metrics?.dollarsPerMile != null && metrics.dollarsPerMile >= settings.minDollarsPerMile)
    val dphOk = (metrics?.dollarsPerHour != null && metrics.dollarsPerHour >= settings.minDollarsPerHour)

    val borderWidth = if (dpmOk && dphOk) 2.dp else 1.dp
    val alpha = settings.overlayOpacity.coerceIn(0.30f, 1.0f)
    val fontScale = settings.fontScale.coerceIn(0.7f, 1.5f)

    var x by remember { mutableStateOf(startX) }
    var y by remember { mutableStateOf(startY) }

    Column(
        modifier = Modifier
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    x = (x + dragAmount.x.roundToInt()).coerceAtLeast(0)
                    y = (y + dragAmount.y.roundToInt()).coerceAtLeast(0)
                    onMove(x, y)
                }
            }
            .background(MaterialTheme.colorScheme.surface.copy(alpha = alpha))
            .border(borderWidth, MaterialTheme.colorScheme.outline)
            .padding(if (settings.compactMode) 8.dp else 12.dp)
    ) {
        Text(
            text = buildString {
                append("Overlay")
                if (offer?.isStacked == true) append(" • STACKED")
            },
            style = MaterialTheme.typography.labelLarge.copy(fontSize = (12.sp.value * fontScale).sp)
        )

        val payout = offer?.payoutDollars
        val miles = offer?.miles
        val timeMins = metrics?.estimatedTotalMinutes

        val payStr = payout?.let { "$" + "%.2f".format(it) } ?: "—"
        val milesStr = miles?.let { "%.1f".format(it) } ?: "—"
        val dpmStr = metrics?.dollarsPerMile?.let { "%.2f".format(it) } ?: "—"
        val dphStr = metrics?.dollarsPerHour?.let { "%.0f".format(it) } ?: "—"
        val timeStr = timeMins?.let { "${it}m" } ?: "—"

        Spacer(Modifier.height(6.dp))
        Text("Pay: $payStr", fontSize = (13.sp.value * fontScale).sp)
        Text("Miles: $milesStr", fontSize = (13.sp.value * fontScale).sp)
        Text("$/mi: $dpmStr", fontSize = (13.sp.value * fontScale).sp)
        Text("$/hr: $dphStr", fontSize = (13.sp.value * fontScale).sp)
        Text("Time: $timeStr", fontSize = (13.sp.value * fontScale).sp)

        if (settings.showDebugText) {
            Spacer(Modifier.height(6.dp))
            val dbg = offer?.capturedText?.replace("\n", " • ")?.take(240).orEmpty()
            Text(dbg, fontSize = (10.sp.value * fontScale).sp)
        }
    }
}
