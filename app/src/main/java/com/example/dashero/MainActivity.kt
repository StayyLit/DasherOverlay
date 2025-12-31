package com.example.dashero

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.example.dashero.data.SettingsRepo
import com.example.dashero.ui.AppNav

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repo = SettingsRepo(this)

        setContent {
            MaterialTheme {
                AppNav(repo = repo)
            }
        }
    }
}
