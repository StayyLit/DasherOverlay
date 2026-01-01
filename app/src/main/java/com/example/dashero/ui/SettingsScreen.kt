package com.example.dashero.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.dashero.data.SettingsRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
  repo: SettingsRepo,
  onBack: () -> Unit
) {
  val s by repo.settingsFlow.collectAsState(
    initial = com.example.dashero.data.UserSettings()
  )
  val scope = remember { CoroutineScope(Dispatchers.Main) }

  fun update(block: (com.example.dashero.data.UserSettings) -> com.example.dashero.data.UserSettings) {
    scope.launch { repo.update(block) }
  }

  Scaffold(
    topBar = { TopAppBar(title = { Text("Settings") }) }
  ) { padding ->
    Column(
      modifier = Modifier
        .padding(padding)
        .padding(16.dp)
        .verticalScroll(rememberScrollState()),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      Text("Offer math")
      IntField(
        label = "Base minutes (pickup/dropoff overhead)",
        value = s.baseMinutes,
        onValue = { update { it.copy(baseMinutes = it.baseMinutes.coerceAtLeast(0).let { _ -> it.baseMinutes } ) } } // placeholder
      )
      IntField(
        label = "Base minutes",
        value = s.baseMinutes,
        onValue = { v -> update { it.copy(baseMinutes = v.coerceAtLeast(0)) } }
      )
      DoubleField(
        label = "Minutes per mile",
        value = s.minutesPerMile,
        onValue = { v -> update { it.copy(minutesPerMile = v.coerceAtLeast(0.0)) } }
      )
      IntField(
        label = "Avg wait minutes",
        value = s.avgWaitMinutes,
        onValue = { v -> update { it.copy(avgWaitMinutes = v.coerceAtLeast(0)) } }
      )

      Divider()

      Text("Thresholds")
      DoubleField(
        label = "Min $/mile",
        value = s.minDollarsPerMile,
        onValue = { v -> update { it.copy(minDollarsPerMile = v.coerceAtLeast(0.0)) } }
      )
      DoubleField(
        label = "Min $/hour",
        value = s.minDollarsPerHour,
        onValue = { v -> update { it.copy(minDollarsPerHour = v.coerceAtLeast(0.0)) } }
      )

      Divider()

      Text("Overlay")
      FloatField(
        label = "Overlay opacity (0.1 - 1.0)",
        value = s.overlayOpacity,
        onValue = { v -> update { it.copy(overlayOpacity = v.coerceIn(0.1f, 1.0f)) } }
      )
      FloatField(
        label = "Font scale (0.5 - 2.0)",
        value = s.fontScale,
        onValue = { v -> update { it.copy(fontScale = v.coerceIn(0.5f, 2.0f)) } }
      )
      SwitchRow(
        label = "Compact mode",
        checked = s.compactMode,
        onChecked = { v -> update { it.copy(compactMode = v) } }
      )
      SwitchRow(
        label = "Show debug text",
        checked = s.showDebugText,
        onChecked = { v -> update { it.copy(showDebugText = v) } }
      )

      Divider()

      Text("Target app")
      OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = s.targetPackage,
        onValueChange = { txt -> update { it.copy(targetPackage = txt) } },
        label = { Text("Target package") },
        singleLine = true
      )

      Divider()

      SwitchRow(
        label = "Accessibility consent",
        checked = s.accessibilityConsent,
        onChecked = { v -> update { it.copy(accessibilityConsent = v) } }
      )

      Spacer(Modifier.height(8.dp))
      Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Back") }
      Spacer(Modifier.height(20.dp))
    }
  }
}

@Composable
private fun SwitchRow(
  label: String,
  checked: Boolean,
  onChecked: (Boolean) -> Unit
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Text(label)
    Switch(checked = checked, onCheckedChange = onChecked)
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun IntField(
  label: String,
  value: Int,
  onValue: (Int) -> Unit
) {
  OutlinedTextField(
    modifier = Modifier.fillMaxWidth(),
    value = value.toString(),
    onValueChange = { txt ->
      txt.toIntOrNull()?.let(onValue)
    },
    label = { Text(label) },
    singleLine = true,
    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DoubleField(
  label: String,
  value: Double,
  onValue: (Double) -> Unit
) {
  OutlinedTextField(
    modifier = Modifier.fillMaxWidth(),
    value = value.toString(),
    onValueChange = { txt ->
      txt.toDoubleOrNull()?.let(onValue)
    },
    label = { Text(label) },
    singleLine = true,
    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FloatField(
  label: String,
  value: Float,
  onValue: (Float) -> Unit
) {
  OutlinedTextField(
    modifier = Modifier.fillMaxWidth(),
    value = value.toString(),
    onValueChange = { txt ->
      txt.toFloatOrNull()?.let(onValue)
    },
    label = { Text(label) },
    singleLine = true,
    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
  )
}
