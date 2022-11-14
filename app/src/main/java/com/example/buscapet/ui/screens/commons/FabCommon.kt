package com.example.buscapet.ui.screens.commons

import android.content.Context
import android.widget.Toast
import androidx.compose.material.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.example.buscapet.R

@Composable
fun FabCommon(context: Context) {
    FloatingActionButton(
        onClick = {
            Toast.makeText(context, "LAWEA", Toast.LENGTH_SHORT).show()
        }, backgroundColor = MaterialTheme.colorScheme.background
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_report),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}