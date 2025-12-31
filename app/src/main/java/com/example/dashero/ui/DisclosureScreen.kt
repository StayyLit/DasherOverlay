package com.example.dashero.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dashero.R
import com.example.dashero.data.SettingsRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun DisclosureScreen(
    onAgree: () -> Unit,
    repo: SettingsRepo
) {
    val scope = CoroutineScope(Dispatchers.Main)

    Scaffold(topBar = {
        TopAppBar(title = { Text(stringResource(id = R.string.disclosure_title)) })
    }) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(stringResource(id = R.string.disclosure_body))
            Button(onClick = {
                scope.launch {
                    repo.update { it.copy(accessibilityConsent = true) }
                    onAgree()
                }
            }) {
                Text(stringResource(id = R.string.consent_button))
            }
        }
    }
}
