package com.example.dashero.parsing

data class ParsedOffer(
    val payoutDollars: Double?,
    val miles: Double?,
    val etaMinutes: Int?,
    val isStacked: Boolean,
    val capturedText: String
)
