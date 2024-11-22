package com.example.buscapet.core.presentation

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.buscapet.ui.theme.BuscaPetTheme

@Composable
fun CommonOutlinedTextField(
    modifier: Modifier = Modifier,
    label: String = "no label",
    inputText: MutableState<String> = mutableStateOf(""),
    enabled: Boolean = true,
    isValid: MutableState<Boolean> = mutableStateOf(true),
    keyOption: KeyboardOptions = KeyboardOptions.Default
) {
    var text by remember { inputText }
    var validity by remember { isValid }
    OutlinedTextField(
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
        keyboardOptions = keyOption,
    )
}

@Composable
fun CommonOutlinedTextFieldWithValidation(
    modifier: Modifier = Modifier,
    label: String?,
    value: String?,
    enabled: Boolean = true,
    isError: Boolean = false,
    errorMessage: String? = null,
    keyOption: KeyboardOptions = KeyboardOptions.Default,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(0.dp, 14.dp, 0.dp, 0.dp),
        value = value.orEmpty(),
        onValueChange = { input ->
            onValueChange(input)
        },
        label = { Text(text = label.orEmpty()) },
        enabled = enabled,
        isError = isError,
        supportingText = {
            if (isError) Text(text = errorMessage.orEmpty())
        },
        keyboardOptions = keyOption,
    )
}

@Preview(uiMode = UI_MODE_NIGHT_YES, name = "PreviewDARK")
@Preview(uiMode = UI_MODE_NIGHT_NO, name = "PreviewLIGHT")
@Composable
fun CommonOutlinedTextFieldPreview() {
    BuscaPetTheme {
        CommonOutlinedTextField()
    }
}