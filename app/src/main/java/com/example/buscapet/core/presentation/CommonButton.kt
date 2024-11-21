package com.example.buscapet.core.presentation

import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CommonButtonWithProgress(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    enabled: Boolean = true,
    isLoading: Boolean = false
) {
    Button(
        onClick = { onClick() },
        enabled = enabled,
//        modifier = Modifier
//            .height(50.dp)
//            .width(120.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.width(35.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                trackColor = MaterialTheme.colorScheme.primary,
            )
        } else
            Text(
                text = "Reportar",
                color = MaterialTheme.colorScheme.onPrimary
            )
    }
}

@Preview
@Composable
fun Preview(modifier: Modifier = Modifier) {
}