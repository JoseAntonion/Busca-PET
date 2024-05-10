package com.example.buscapet.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.buscapet.ui.navigation.Navigation
import com.example.buscapet.ui.theme.BuscaPetTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BuscaPetTheme {
                Navigation(navHostController = rememberNavController())
            }
        }
    }

}