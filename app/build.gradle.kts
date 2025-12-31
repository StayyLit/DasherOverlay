plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.android")
  id("org.jetbrains.kotlin.plugin.compose")
}

android {
    compileSdk = 34

    defaultConfig {
        minSdk = 26
        targetSdk = 34
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}


  buildFeatures { compose = true }
  composeOptions { kotlinCompilerExtensionVersion = "1.5.15" }

  kotlinOptions { jvmTarget = "17" }

  packaging {
    resources.excludes += setOf(
      "META-INF/AL2.0",
      "META-INF/LGPL2.1"
    )
  }
}

dependencies {
  implementation(platform("androidx.compose:compose-bom:2024.10.01"))
  implementation("androidx.activity:activity-compose:1.9.3")
  implementation("androidx.compose.ui:ui")
  implementation("androidx.compose.ui:ui-tooling-preview")
  debugImplementation("androidx.compose.ui:ui-tooling")
  implementation("androidx.compose.material3:material3")

  implementation("androidx.navigation:navigation-compose:2.8.3")

  // DataStore
  implementation("androidx.datastore:datastore-preferences:1.1.1")

  // Coroutines
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")

  // Lifecycle
  implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.6")
}
