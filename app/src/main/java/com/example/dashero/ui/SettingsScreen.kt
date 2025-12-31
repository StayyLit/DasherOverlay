package com.example.dashero.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dashero.data.SettingsRepo
import com.example.dashero.data.UserSettings
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(repo: SettingsRepo, onBack: () -> Unit) {
    val scope = rememberCoroutineScope()
    val settings by repo.settingsFlow.collectAsState(initial = UserSettings())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = { TextButton(onClick = onBack) { Text("Back") } }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text("Estimation")
            StepperInt("Base minutes", settings.baseMinutes, 0, 60) { v ->
                scope.launch { repo.update { it.copy(baseMinutes = v) } }
            }
            StepperInt("Avg wait minutes", settings.avgWaitMinutes, 0, 30) { v ->
                scope.launch { repo.update { it.copy(avgWaitMinutes = v) } }
            }
            SliderDouble(
                label = "Minutes per mile",
                value = settings.minutesPerMile,
                min = 0.5,
                max = 10.0,
                step = 0.5
            ) { v ->
                scope.launch { repo.update { it.copy(minutesPerMile = v) } }
            }

            Divider()
            Text("Rules")
            SliderDouble("Min $/mile", settings.minDollarsPerMile, 0.0, 5.0, 0.1) { v ->
                scope.launch { repo.update { it.copy(minDollarsPerMile = v) } }
            }
            SliderDouble("Min $/hour", settings.minDollarsPerHour, 0.0, 60.0, 1.0) { v ->
                scope.launch { repo.update { it.copy(minDollarsPerHour = v) } }
            }

            Divider()
            Text("Overlay UI")
            SwitchRow("Compact mode", settings.compactMode) { b ->
                scope.launch { repo.update { it.copy(compactMode = b) } }
            }
            SliderFloat("Opacity", settings.overlayOpacity, 0.30f, 1.0f, 0.02f) { v ->
                scope.launch { repo.update { it.copy(overlayOpacity = v) } }
            }
            SliderFloat("Font scale", settings.fontScale, 0.7f, 1.5f, 0.05f) { v ->
                scope.launch { repo.update { it.copy(fontScale = v) } }
            }

            Divider()
            Text("Advanced")
            SwitchRow("Show debug text", settings.showDebugText) { b ->
                scope.launch { repo.update { it.copy(showDebugText = b) } }
            }
            OutlinedTextField(
                value = settings.targetPackage,
                onValueChange = { new ->
                    scope.launch { repo.update { it.copy(targetPackage = new) } }
                },
                label = { Text("Target package (optional)") },
                supportingText = { Text("Leave as com.doordash.driverapp, or empty to parse all apps (not recommended).") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun SwitchRow(label: String, value: Boolean, onChange: (Boolean) -> Unit) {
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
        Text(label, modifier = Modifier.weight(1f))
        Switch(checked = value, onCheckedChange = onChange)
    }
}

@Composable
private fun StepperInt(label: String, value: Int, min: Int, max: Int, onChange: (Int) -> Unit) {
    Column {
        Text("$label: $value")
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            OutlinedButton(onClick = { onChange((value - 1).coerceAtLeast(min)) }) { Text("-") }
            OutlinedButton(onClick = { onChange((value + 1).coerceAtMost(max)) }) { Text("+") }
        }
    }
}

@Composable
private fun SliderDouble(label: String, value: Double, min: Double, max: Double, step: Double, onChange: (Double) -> Unit) {
    val steps = ((max - min) / step).toInt().coerceAtLeast(0)
    Column {
        Text("$label: ${"%.2f".format(value)}")
        Slider(
            value = value.toFloat(),
            onValueChange = { onChange(it.toDouble()) },
            valueRange = min.toFloat()..max.toFloat(),
            steps = steps - 1
        )
    }
}

@Composable
private fun SliderFloat(label: String, value: Float, min: Float, max: Float, step: Float, onChange: (Float) -> Unit) {
    val steps = (((max - min) / step).toInt()).coerceAtLeast(0)
    Column {
        Text("$label: ${"%.2f".format(value)}")
        Slider(
            value = value,
            onValueChange = { onChange(it) },
            valueRange = min..max,
            steps = steps - 1
        )
    }
}
