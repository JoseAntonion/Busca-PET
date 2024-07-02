package com.example.buscapet.ui.commons

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CommonFilledTextField(
    modifier: Modifier = Modifier,
    label: String = "no label",
    inputText: MutableState<String> = mutableStateOf(""),
    enabled: Boolean = true,
    isValid: MutableState<Boolean> = mutableStateOf(true),
    keyOption: KeyboardOptions = KeyboardOptions.Default
) {
    var text by remember { inputText }
    var validity by remember { isValid }
    TextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(0.dp, 14.dp, 0.dp, 0.dp),
        value = text,
        onValueChange = { input ->
            text = input
            validity = input.isNotEmpty()
        },
        label = { Text(text = label) },
        enabled = enabled,
        isError = !validity,
        keyboardOptions = keyOption
    )
}