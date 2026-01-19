package com.example.buscapet.add_pet.domain.use_case

import android.graphics.Bitmap
import com.example.buscapet.core.domain.classification.ClassificationResult
import com.example.buscapet.core.domain.classification.ImageClassifier
import javax.inject.Inject

class AnalyzePetImageUseCase @Inject constructor(
    private val imageClassifier: ImageClassifier
) {
    suspend operator fun invoke(bitmap: Bitmap): List<ClassificationResult> {
        return imageClassifier.classify(bitmap, maxResults = 3)
    }
}