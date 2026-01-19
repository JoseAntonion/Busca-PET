package com.example.buscapet.core.domain.classification

import android.graphics.Bitmap

interface ImageClassifier {
    suspend fun classify(bitmap: Bitmap, maxResults: Int): List<ClassificationResult>
}

data class ClassificationResult(
    val label: String,
    val score: Float
)