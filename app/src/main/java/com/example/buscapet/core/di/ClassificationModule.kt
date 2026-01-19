package com.example.buscapet.core.di

import com.example.buscapet.core.data.classification.TfLiteImageClassifier
import com.example.buscapet.core.domain.classification.ImageClassifier
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ClassificationModule {

    @Binds
    @Singleton
    abstract fun bindImageClassifier(
        tfLiteImageClassifier: TfLiteImageClassifier
    ): ImageClassifier
}