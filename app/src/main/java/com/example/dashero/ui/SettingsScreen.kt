package com.example.dashero.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
  val scope = CoroutineScope(Dispatchers.Main)
  val settings by repo.settingsFlow.collectAsState(initial = repo.defaultSettings())

  Scaffold(
    topBar = {
      TopAppBar(
        title = { Text("Settings") }
      )
    }
  ) { padding ->
    Surface(modifier = Modifier.padding(padding)) {
      Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        Text("Overlay Settings", style = MaterialTheme.typography.titleMedium)

        RowSwitch(
          title = "Show $/mile",
          checked = settings.showPerMile,
          onCheckedChange = { v ->
            scope.launch { repo.update { it.copy(showPerMile = v) } }
          }
        )

        RowSwitch(
          title = "Show $/hour",
          checked = settings.showPerHour,
          onCheckedChange = { v ->
            scope.launch { repo.update { it.copy(showPerHour = v) } }
          }
        )

        RowSwitch(
          title = "Show time estimate",
          checked = settings.showTime,
          onCheckedChange = { v ->
            scope.launch { repo.update { it.copy(showTime = v) } }
          }
        )

        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("Back")
          }
        }
      }
    }
  }
}

@Composable
private fun RowSwitch(
  title: String,
  checked: Boolean,
  onCheckedChange: (Boolean) -> Unit
) {
  androidx.compose.foundation.layout.Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Text(title, modifier = Modifier.padding(end = 12.dp))
    Switch(checked = checked, onCheckedChange = onCheckedChange)
  }
}
