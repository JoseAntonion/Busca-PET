package com.example.buscapet.ui.ui.my_reports

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun MyReportsScreen() {
    val userName = FirebaseAuth.getInstance().currentUser?.displayName
    MainView(
        userName = userName
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewMainView() {
    MainView(
        userName = "user prueba"
    )
}

@Composable
fun MainView(
    userName: String?
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "My Reports",
            textAlign = TextAlign.Center
        )
    }
}