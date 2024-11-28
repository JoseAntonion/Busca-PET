package com.example.buscapet.core.presentation

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.buscapet.ui.theme.BuscaPetTheme

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

@Composable
fun CommonLongButton(
    text: String = "",
    textColor: Color = Color.Unspecified,
    customButtonColors: ButtonColors = ButtonDefaults.buttonColors(),
    enabled: Boolean = true,
    onClick: () -> Unit = {},
) {
    Button(
        onClick = { onClick() },
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth(),
        colors = customButtonColors,
    ) {
        Text(
            text = text,
            color = textColor
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "PreviewDARK")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "PreviewLIGHT")
@Composable
fun PreviewCommonButtonWithProgress() {
    BuscaPetTheme {
        CommonButtonWithProgress()
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "PreviewDARK")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "PreviewLIGHT")
@Composable
fun PreviewCommonLongButton() {
    BuscaPetTheme {
        CommonLongButton(
            text = "Long Button Text",
            textColor = MaterialTheme.colorScheme.onPrimary,
            customButtonColors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        )
    }
}