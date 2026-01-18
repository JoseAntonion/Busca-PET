package com.example.buscapet.core.presentation.util

import android.content.Context
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

@Composable
fun <T> ObserveAsEvents(
    flow: Flow<T>,
    snackbarHostState: SnackbarHostState? = null,
    onEvent: (T) -> Unit = {}
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    
    LaunchedEffect(flow, lifecycleOwner.lifecycle) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                flow.collect { event ->
                    // Handle global Snackbar events automatically if HostState is provided
                    if (event is CoreUiEvent.ShowSnackbar && snackbarHostState != null) {
                        val message = event.message.asString(context)
                        val action = event.actionLabel?.asString(context)
                        
                        val result = snackbarHostState.showSnackbar(
                            message = message,
                            actionLabel = action,
                            duration = if (action != null) SnackbarDuration.Indefinite else SnackbarDuration.Short
                        )
                        
                        // Pass result back if needed via a callback, 
                        // but for now we just show it.
                        // If we needed to handle "Retry" clicks, we would pass that back to onEvent
                        if (result == SnackbarResult.ActionPerformed) {
                            // This logic would ideally be specific to the screen if needed
                        }
                    }
                    
                    // Always pass the event to the specific handler
                    onEvent(event)
                }
            }
        }
    }
}