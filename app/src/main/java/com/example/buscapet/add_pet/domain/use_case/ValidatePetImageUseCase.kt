package com.example.buscapet.add_pet.domain.use_case

class ValidatePetImageUseCase {
    operator fun invoke(image: String?): ImageValidationResult {
        if (image.isNullOrBlank() || image.isEmpty()) return ImageValidationResult(
            isValid = false,
            error = "La imagen seleccionada esta vacia. Vuelva a intentarlo."
        )
        return ImageValidationResult(isValid = true)
    }
}