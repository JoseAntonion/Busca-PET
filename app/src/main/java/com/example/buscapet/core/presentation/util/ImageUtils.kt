package com.example.buscapet.core.presentation.util

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun rememberBitmapFromBase64(base64String: String?): ImageBitmap? {
    return remember(base64String) {
        if (base64String.isNullOrEmpty()) return@remember null
        try {
            val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            bitmap?.asImageBitmap()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

@Composable
fun Base64Image(
    base64String: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
    placeholder: @Composable () -> Unit = {}
) {
    var imageBitmap by remember(base64String) { mutableStateOf<ImageBitmap?>(null) }
    var isLoading by remember(base64String) { mutableStateOf(true) }

    LaunchedEffect(base64String) {
        if (base64String.isNullOrEmpty()) {
            imageBitmap = null
            isLoading = false
            return@LaunchedEffect
        }
        
        isLoading = true
        val bitmap = withContext(Dispatchers.Default) {
            try {
                val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
                val androidBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                androidBitmap?.asImageBitmap()
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
        imageBitmap = bitmap
        isLoading = false
    }

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            if (imageBitmap != null) {
                Image(
                    bitmap = imageBitmap!!,
                    contentDescription = contentDescription,
                    contentScale = contentScale,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                placeholder()
            }
        }
    }
}