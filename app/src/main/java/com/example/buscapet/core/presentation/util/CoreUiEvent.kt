package com.example.buscapet.core.presentation.util

import com.example.buscapet.core.presentation.model.UiText

sealed interface CoreUiEvent {
    data class ShowSnackbar(
        val message: UiText,
        val actionLabel: UiText? = null
    ) : CoreUiEvent
    
    // Podemos agregar otros eventos comunes aquí en el futuro (ej. Navigate)
}