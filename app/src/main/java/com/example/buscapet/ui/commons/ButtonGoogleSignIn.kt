package com.example.buscapet.ui.commons

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.buscapet.R
import com.example.buscapet.ui.theme.BuscaPetTheme
import com.example.buscapet.ui.theme.GoogleDarkOnSignInButton
import com.example.buscapet.ui.theme.GoogleLightOnSignInButton
import com.example.buscapet.ui.theme.shapes

@Composable
fun SignInButton(
    text: String,
    loadingText: String = "Ingresando...",
    icon: Painter,
    isLoading: Boolean = false,
    shape: Shape = shapes.extraSmall,
    borderColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    progressIndicatorColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .clickable(
                enabled = !isLoading,
                onClick = onClick
            ),
        shape = shape,
        border = BorderStroke(width = 1.dp, color = borderColor),
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier.padding(end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Icon(
                painter = icon,
                contentDescription = "SignInButton",
                tint = Color.Unspecified
            )
            Text(
                modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                text = if (isLoading) loadingText else text,
                color = if (isSystemInDarkTheme()) GoogleDarkOnSignInButton else GoogleLightOnSignInButton
            )
            if (isLoading) {
                //Spacer(modifier = Modifier.width(16.dp))
                CircularProgressIndicator(
                    progress = { 5f },
                    modifier = Modifier
                        .height(18.dp)
                        .width(18.dp),
                    color = progressIndicatorColor,
                    strokeWidth = 2.dp,
                )
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark Mode")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light Mode")
@Composable
fun Preview() {
    BuscaPetTheme {
        SignInButton(
            text = "Sign in with Google",
            loadingText = "Signing in...",
            isLoading = true,
            icon = painterResource(id = R.drawable.btn_google_light_normal_ios),
            onClick = {
                TODO()
            }
        )
    }
}