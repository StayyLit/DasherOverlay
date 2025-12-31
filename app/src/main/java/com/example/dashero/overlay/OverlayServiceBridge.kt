package com.example.dashero.overlay

import com.example.dashero.parsing.ParsedOffer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object OverlayServiceBridge {
    private val _offerFlow = MutableStateFlow<ParsedOffer?>(null)
    val offerFlow = _offerFlow.asStateFlow()

    fun updateOffer(offer: ParsedOffer) {
        _offerFlow.value = offer
    }
}
