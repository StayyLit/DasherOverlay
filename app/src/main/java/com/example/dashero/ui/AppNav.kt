package com.example.dashero.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dashero.data.SettingsRepo

object Routes {
    const val Disclosure = "disclosure"
    const val Home = "home"
    const val Settings = "settings"
}

@Composable
fun AppNav(repo: SettingsRepo) {
    val nav = rememberNavController()
    val settings by repo.settingsFlow.collectAsState(initial = null)

    val start = if (settings?.accessibilityConsent == true) Routes.Home else Routes.Disclosure

    NavHost(navController = nav, startDestination = start) {
        composable(Routes.Disclosure) {
            DisclosureScreen(
                onAgree = {
                    // handled inside screen
                    nav.navigate(Routes.Home) { popUpTo(Routes.Disclosure) { inclusive = true } }
                },
                repo = repo
            )
        }
        composable(Routes.Home) {
            HomeScreen(
                repo = repo,
                onOpenSettings = { nav.navigate(Routes.Settings) }
            )
        }
        composable(Routes.Settings) {
            SettingsScreen(repo = repo, onBack = { nav.popBackStack() })
        }
    }
}
