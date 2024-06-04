package com.example.buscapet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.buscapet.ui.navigation.RootNavGraph
import com.example.buscapet.ui.theme.BuscaPetTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BuscaPetTheme {
                RootNavGraph()
            }
        }
    }

}