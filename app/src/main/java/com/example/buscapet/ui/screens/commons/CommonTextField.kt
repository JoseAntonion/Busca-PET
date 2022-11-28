package com.example.buscapet.ui.screens.commons

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.buscapet.ui.theme.BuscaPetTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun CommonTextField(
    hintText: String,
    data: MutableState<String>,
    modifier: Modifier = Modifier,
    imeAction: ImeAction = ImeAction.Done
) {
    val (currentData, onCurrentDataChange) = data
    val keyboardController = LocalSoftwareKeyboardController.current

    BuscaPetTheme {
        Column(
            modifier = modifier.fillMaxWidth()
        ) {
            TextField(
                value = currentData,
                onValueChange = { onCurrentDataChange(it) },
                label = {
                    Text(text = hintText)
                },
                modifier = Modifier
                    .fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = imeAction),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        // do something here
                    }
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewCommonTextField() {
    val testData = remember { mutableStateOf("wea") }
    CommonTextField(
        hintText = "Ingrese nombre",
        data = testData,
        modifier = Modifier
            .padding(8.dp)
    )
}