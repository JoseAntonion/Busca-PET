package com.example.buscapet.core.domain.model

sealed class ValidationEvent {
    data object Success : ValidationEvent()
}