package com.example.buscapet.add_pet.domain.use_case

import android.graphics.Bitmap
import com.example.buscapet.core.domain.classification.ClassificationResult
import com.example.buscapet.core.domain.classification.ImageClassifier
import com.example.buscapet.core.domain.classification.ModelType
import javax.inject.Inject

class AnalyzePetImageUseCase @Inject constructor(
    private val imageClassifier: ImageClassifier
) {
    suspend operator fun invoke(
        bitmap: Bitmap, 
        modelType: ModelType = ModelType.BREED
    ): List<ClassificationResult> {
        return imageClassifier.classify(bitmap, maxResults = 3, modelType = modelType)
    }
}