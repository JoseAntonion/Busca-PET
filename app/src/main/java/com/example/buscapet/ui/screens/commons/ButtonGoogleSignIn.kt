package com.example.buscapet.ui.screens.commons

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import com.example.buscapet.ui.theme.*

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewSignInButton() {
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

@ExperimentalMaterialApi
@Composable
fun SignInButton(
    text: String,
    loadingText: String = "Ingresando...",
    icon: Painter,
    isLoading: Boolean = false,
    shape: Shape = shapes.extraSmall,
    borderColor: Color = Color.LightGray,
    //backgroundColor: Color = MaterialTheme.colorScheme.surface,
    progressIndicatorColor: Color = MaterialTheme.colorScheme.primary,
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
        color = if (isSystemInDarkTheme()) GoogleDarkSignInButton else GoogleLightSignInButton
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
                    modifier = Modifier
                        .height(18.dp)
                        .width(18.dp),
                    strokeWidth = 2.dp,
                    progress = 5f,
                    color = progressIndicatorColor
                )
            }
        }
    }
}