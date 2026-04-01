package com.example.buscapet.core.presentation

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.buscapet.R
import com.example.buscapet.ui.theme.BuscaPetTheme

enum class GoogleButtonTheme {
    Light, Dark
}

@Composable
fun SignInButton(
    text: String,
    loadingText: String = "Ingresando...",
    icon: Painter,
    isLoading: Boolean = false,
    theme: GoogleButtonTheme = GoogleButtonTheme.Light,
    onClick: () -> Unit
) {
    // Definición de colores según las directrices más recientes de Google Identity (Material 3)
    val backgroundColor = if (theme == GoogleButtonTheme.Light) Color(0xFFFFFFFF) else Color(0xFF131314)
    val contentColor = if (theme == GoogleButtonTheme.Light) Color(0xFF1F1F1F) else Color(0xFFE3E3E3)
    val borderColor = if (theme == GoogleButtonTheme.Light) Color(0xFF747775) else Color(0xFF8E918F)
    
    // Forma estándar de píldora (CircleShape) o ligeramente redondeada. 
    // Google recomienda ahora bordes completamente redondeados o 4dp. Usaremos CircleShape para un look más moderno en Material 3.
    val buttonShape = RoundedCornerShape(percent = 50)

    Surface(
        modifier = Modifier
            .height(40.dp) // Altura recomendada por Material Design
            .clip(buttonShape)
            .clickable(
                enabled = !isLoading,
                onClick = onClick
            ),
        shape = buttonShape,
        border = BorderStroke(width = 1.dp, color = borderColor),
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier.padding(start = 12.dp, end = 16.dp), // Padding asimétrico sugerido (12dp izquierdo por el icono, 16dp derecho)
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Icon(
                painter = icon,
                contentDescription = "Google Logo",
                tint = Color.Unspecified,
                modifier = Modifier.size(20.dp) // Tamaño estándar del logo de Google (18-20dp)
            )
            
            Spacer(modifier = Modifier.width(10.dp))
            
            Text(
                text = if (isLoading) loadingText else text,
                color = contentColor,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Medium // Medium weight de acuerdo a las directrices
                )
            )
            
            if (isLoading) {
                Spacer(modifier = Modifier.width(12.dp))
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    color = contentColor,
                    strokeWidth = 2.dp,
                )
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark Mode")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light Mode")
@Composable
fun PreviewGoogleSignInButton() {
    BuscaPetTheme {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            SignInButton(
                text = "Ingresar con Google",
                icon = painterResource(id = R.drawable.btn_google_light_normal_ios),
                theme = GoogleButtonTheme.Light,
                onClick = {}
            )
            
            SignInButton(
                text = "Ingresar con Google",
                icon = painterResource(id = R.drawable.btn_google_light_normal_ios),
                theme = GoogleButtonTheme.Dark,
                onClick = {}
            )
            
            SignInButton(
                text = "Ingresar con Google",
                isLoading = true,
                icon = painterResource(id = R.drawable.btn_google_light_normal_ios),
                theme = GoogleButtonTheme.Light,
                onClick = {}
            )
        }
    }
}