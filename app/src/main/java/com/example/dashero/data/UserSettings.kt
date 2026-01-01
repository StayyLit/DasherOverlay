package com.example.dashero.data

data class UserSettings(
  val baseMinutes: Int = 8,
  val minutesPerMile: Double = 3.0,
  val avgWaitMinutes: Int = 3,

  val minDollarsPerMile: Double = 1.50,
  val minDollarsPerHour: Double = 18.0,

  val compactMode: Boolean = false,
  val overlayOpacity: Float = 0.92f,
  val fontScale: Float = 1.0f,
  val showDebugText: Boolean = false,

  val targetPackage: String = "com.doordash.driverapp",
  val accessibilityConsent: Boolean = false
)
