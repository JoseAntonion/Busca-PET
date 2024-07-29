package com.example.buscapet.report.domain.use_case

class ValidateTextFieldUseCase {
    operator fun invoke(text: String?): FormValidationResult {
        if (text.isNullOrEmpty() || text.isBlank()) return FormValidationResult(
            isValid = false,
            error = "Este campo no puede estar vacio"
        )
        return FormValidationResult(isValid = true)
    }
}