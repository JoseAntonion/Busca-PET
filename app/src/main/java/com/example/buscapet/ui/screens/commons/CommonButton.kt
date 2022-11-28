package com.example.buscapet.ui.screens.commons

import android.content.res.Configuration
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.buscapet.ui.theme.BuscaPetTheme

@Composable
fun CommonButton(
    modifier: Modifier = Modifier,
    text: String = "Label"
) {
    BuscaPetTheme {
        Button(onClick = { /*TODO*/ }, modifier = modifier) {
            Text(text = text)
        }
    }
}

@Composable
fun CommonOutlinedButton(
    text: String = "Label"
) {
    BuscaPetTheme {
        OutlinedButton(onClick = { /*TODO*/ }) {
            Text(text = text)
        }
    }
}

@Composable
fun CommonElevatedButton(
    text: String = "Label"
) {
    BuscaPetTheme {
        ElevatedButton(onClick = { /*TODO*/ }) {
            Text(text = text)
        }
    }
}

@Composable
fun CommonFilledTonalButton(
    text: String = "Label"
) {
    BuscaPetTheme {
        FilledTonalButton(onClick = { /*TODO*/ }) {
            Text(text = text)
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FilledTonalButton() {
    CommonFilledTonalButton("Reportar")
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ElevatedButton() {
    CommonElevatedButton("Reportar")
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun OutlinedButton() {
    CommonOutlinedButton("Reportar")
}

@Preview(showBackground = true, name = "Light")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark")
@Composable
fun Button() {
    CommonButton(text = "Reportar")
}