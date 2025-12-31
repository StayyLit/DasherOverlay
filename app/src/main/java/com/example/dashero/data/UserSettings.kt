package com.example.dashero.data

data class UserSettings(
    val baseMinutes: Int = 8,
    val minutesPerMile: Double = 3.0,
    val avgWaitMinutes: Int = 3,

    val minDollarsPerMile: Double = 1.50,
    val minDollarsPerHour: Double = 18.0,

    val compactMode: Boolean = false,
    val overlayOpacity: Float = 0.92f, // 0.30 .. 1.0
    val fontScale: Float = 1.0f,       // 0.7 .. 1.5

    val showDebugText: Boolean = false,

    // Package gating: restrict parsing to this package when non-empty.
    // DoorDash Driver is commonly "com.doordash.driverapp", but allow user override.
    val targetPackage: String = "com.doordash.driverapp",

    // Consent gate
    val accessibilityConsent: Boolean = false
)
