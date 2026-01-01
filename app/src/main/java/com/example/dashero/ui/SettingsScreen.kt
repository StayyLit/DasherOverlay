package com.example.dashero.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dashero.data.SettingsRepo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
  repo: SettingsRepo,
  onBack: () -> Unit
) {
  // Repo is intentionally unused until we align this screen to your actual Settings model.
  Scaffold(
    topBar = { TopAppBar(title = { Text("Settings") }) }
  ) { padding ->
    Column(
      modifier = Modifier.padding(padding).padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      Text("Settings screen placeholder (compile-first).")
      Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Back") }
    }
  }
}
