package com.example.dashero.calc

import com.example.dashero.parsing.ParsedOffer
import kotlin.math.roundToInt

object MetricsCalculator {

    /**
     * Time estimate:
     * - If ETA is visible, use it.
     * - Else: base + avgWait + (minutesPerMile * miles)
     */
    fun compute(
        offer: ParsedOffer,
        baseMinutes: Int,
        minutesPerMile: Double,
        avgWaitMinutes: Int
    ): Metrics {
        val totalMinutes = when {
            offer.etaMinutes != null -> offer.etaMinutes
            offer.miles != null -> (baseMinutes + avgWaitMinutes + minutesPerMile * offer.miles).roundToInt()
            else -> null
        }

        val dpm = if (offer.payoutDollars != null && offer.miles != null && offer.miles > 0) {
            offer.payoutDollars / offer.miles
        } else null

        val dph = if (offer.payoutDollars != null && totalMinutes != null && totalMinutes > 0) {
            offer.payoutDollars * (60.0 / totalMinutes.toDouble())
        } else null

        return Metrics(dpm, dph, totalMinutes)
    }
}
