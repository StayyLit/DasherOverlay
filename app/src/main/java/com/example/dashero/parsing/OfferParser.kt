package com.example.dashero.parsing

object OfferParser {

    // Money: allow "$12.50" or "12.50"
    private val moneyRegex = Regex("""\$\s?(\d+(?:\.\d{1,2})?)""")
    private val milesRegex = Regex("""(\d+(?:\.\d+)?)\s?mi\b""", RegexOption.IGNORE_CASE)
    private val minutesRegex = Regex("""(\d{1,3})\s?min\b""", RegexOption.IGNORE_CASE)

    // Stacked/add-on heuristics (keep flexible)
    private val stackedWords = listOf(
        "2 orders", "two orders", "additional order", "add-on", "addon", "stacked", "bundle"
    )

    fun parse(allText: String): ParsedOffer {
        val normalized = allText.lowercase()

        // Payout: choose the largest $ value on screen (often the offer amount)
        val payout = moneyRegex.findAll(allText)
            .mapNotNull { it.groupValues.getOrNull(1)?.toDoubleOrNull() }
            .maxOrNull()

        // Miles: first match usually fine
        val miles = milesRegex.find(allText)?.groupValues?.getOrNull(1)?.toDoubleOrNull()

        // ETA: if multiple mins exist, pick the largest as total time guess
        val eta = minutesRegex.findAll(allText)
            .mapNotNull { it.groupValues.getOrNull(1)?.toIntOrNull() }
            .maxOrNull()

        val isStacked = stackedWords.any { normalized.contains(it) }

        return ParsedOffer(
            payoutDollars = payout,
            miles = miles,
            etaMinutes = eta,
            isStacked = isStacked,
            capturedText = allText
        )
    }
}
