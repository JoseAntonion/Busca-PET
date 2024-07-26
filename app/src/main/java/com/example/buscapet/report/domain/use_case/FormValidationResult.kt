package com.example.buscapet.report.domain.use_case

data class FormValidationResult(
    val isValid: Boolean = false,
    val error: String? = null
)