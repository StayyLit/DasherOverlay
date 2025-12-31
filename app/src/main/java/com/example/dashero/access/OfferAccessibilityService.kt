package com.example.dashero.access

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import com.example.dashero.data.SettingsRepo
import com.example.dashero.overlay.OverlayServiceBridge
import com.example.dashero.parsing.OfferParser
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first

class OfferAccessibilityService : AccessibilityService() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val root = rootInActiveWindow ?: return
        val pkgName = event?.packageName?.toString().orEmpty()

        scope.launch {
            val repo = SettingsRepo(applicationContext)
            val settings = repo.settingsFlow.first()

            // Consent gate
            if (!settings.accessibilityConsent) return@launch

            // Package gating if set
            val target = settings.targetPackage.trim()
            if (target.isNotEmpty() && pkgName.isNotEmpty() && pkgName != target) return@launch

            val text = OfferTextAggregator.collectText(root)
            if (text.isBlank()) return@launch

            val parsed = OfferParser.parse(text)
            OverlayServiceBridge.updateOffer(parsed)
        }
    }

    override fun onInterrupt() = Unit

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }
}
