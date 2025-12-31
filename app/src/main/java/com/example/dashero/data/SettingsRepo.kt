package com.example.dashero.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.ds by preferencesDataStore(name = "settings")

class SettingsRepo(private val ctx: Context) {

    private object Keys {
        val baseMinutes = intPreferencesKey("baseMinutes")
        val minutesPerMile = doublePreferencesKey("minutesPerMile")
        val avgWaitMinutes = intPreferencesKey("avgWaitMinutes")

        val minDollarsPerMile = doublePreferencesKey("minDollarsPerMile")
        val minDollarsPerHour = doublePreferencesKey("minDollarsPerHour")

        val compactMode = booleanPreferencesKey("compactMode")
        val overlayOpacity = floatPreferencesKey("overlayOpacity")
        val fontScale = floatPreferencesKey("fontScale")
        val showDebugText = booleanPreferencesKey("showDebugText")

        val targetPackage = stringPreferencesKey("targetPackage")
        val accessibilityConsent = booleanPreferencesKey("accessibilityConsent")

        val overlayX = intPreferencesKey("overlayX")
        val overlayY = intPreferencesKey("overlayY")
    }

    val settingsFlow: Flow<UserSettings> = ctx.ds.data.map { p ->
        UserSettings(
            baseMinutes = p[Keys.baseMinutes] ?: 8,
            minutesPerMile = p[Keys.minutesPerMile] ?: 3.0,
            avgWaitMinutes = p[Keys.avgWaitMinutes] ?: 3,

            minDollarsPerMile = p[Keys.minDollarsPerMile] ?: 1.50,
            minDollarsPerHour = p[Keys.minDollarsPerHour] ?: 18.0,

            compactMode = p[Keys.compactMode] ?: false,
            overlayOpacity = p[Keys.overlayOpacity] ?: 0.92f,
            fontScale = p[Keys.fontScale] ?: 1.0f,

            showDebugText = p[Keys.showDebugText] ?: false,
            targetPackage = p[Keys.targetPackage] ?: "com.doordash.driverapp",
            accessibilityConsent = p[Keys.accessibilityConsent] ?: false
        )
    }

    suspend fun update(transform: (UserSettings) -> UserSettings) {
        ctx.ds.edit { p ->
            val current = UserSettings(
                baseMinutes = p[Keys.baseMinutes] ?: 8,
                minutesPerMile = p[Keys.minutesPerMile] ?: 3.0,
                avgWaitMinutes = p[Keys.avgWaitMinutes] ?: 3,
                minDollarsPerMile = p[Keys.minDollarsPerMile] ?: 1.50,
                minDollarsPerHour = p[Keys.minDollarsPerHour] ?: 18.0,
                compactMode = p[Keys.compactMode] ?: false,
                overlayOpacity = p[Keys.overlayOpacity] ?: 0.92f,
                fontScale = p[Keys.fontScale] ?: 1.0f,
                showDebugText = p[Keys.showDebugText] ?: false,
                targetPackage = p[Keys.targetPackage] ?: "com.doordash.driverapp",
                accessibilityConsent = p[Keys.accessibilityConsent] ?: false
            )
            val next = transform(current)

            p[Keys.baseMinutes] = next.baseMinutes
            p[Keys.minutesPerMile] = next.minutesPerMile
            p[Keys.avgWaitMinutes] = next.avgWaitMinutes

            p[Keys.minDollarsPerMile] = next.minDollarsPerMile
            p[Keys.minDollarsPerHour] = next.minDollarsPerHour

            p[Keys.compactMode] = next.compactMode
            p[Keys.overlayOpacity] = next.overlayOpacity
            p[Keys.fontScale] = next.fontScale
            p[Keys.showDebugText] = next.showDebugText

            p[Keys.targetPackage] = next.targetPackage
            p[Keys.accessibilityConsent] = next.accessibilityConsent
        }
    }

    fun overlayPosFlow(): Flow<Pair<Int, Int>> =
        ctx.ds.data.map { p -> (p[Keys.overlayX] ?: 24) to (p[Keys.overlayY] ?: 200) }

    suspend fun saveOverlayPos(x: Int, y: Int) {
        ctx.ds.edit { p ->
            p[Keys.overlayX] = x
            p[Keys.overlayY] = y
        }
    }
}
