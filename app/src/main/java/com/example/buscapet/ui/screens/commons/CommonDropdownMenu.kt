package com.example.buscapet.ui.screens.commons

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun CommonDropdownMenu() {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.TopStart)
    ) {
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Localized description"
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            CommonDropdownMenuItem(
                text = "Edit",
                onClick = { /* Handle send feedback! */ },
                leadingIcon = Icons.Outlined.Edit,
                {}
            )
            CommonDropdownMenuItem(
                text = "Settings",
                onClick = { /* Handle send feedback! */ },
                leadingIcon = Icons.Outlined.Settings,
                {}
            )
            Divider()
            CommonDropdownMenuItem(
                text = "Send Feedback",
                onClick = { /* Handle send feedback! */ },
                leadingIcon = Icons.Outlined.Email,
                trailingIcon = { Text("F11", textAlign = TextAlign.Center) }
            )
        }
    }
}

@Composable
fun CommonDropdownMenuItem(
    text: String,
    onClick: () -> Unit,
    leadingIcon: ImageVector,
    trailingIcon: @Composable () -> Unit
) {
    DropdownMenuItem(
        text = { Text(text) },
        onClick = { onClick() },
        leadingIcon = {
            Icon(leadingIcon, null)
        },
        trailingIcon = { trailingIcon }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewCommonDropdownMenuItem() {
    CommonDropdownMenuItem(
        text = "Item prueba 1",
        {},
        leadingIcon = Icons.Outlined.Email,
        trailingIcon = { Text("F11", textAlign = TextAlign.Center) }
    )
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewCommonDropdownMenu() {
    CommonDropdownMenu()
}