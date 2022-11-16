package com.example.buscapet.ui.util

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.Composable

@Composable
fun Context.CloseApp() = (this as? Activity)?.finishAffinity()