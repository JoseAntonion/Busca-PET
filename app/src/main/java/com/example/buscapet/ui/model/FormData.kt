package com.example.buscapet.ui.model

import androidx.compose.runtime.mutableStateOf

class FormData {
    var color = mutableStateOf("")
    var size = mutableStateOf("")
    var breed = mutableStateOf("")
    var geoLoc = mutableStateOf("")
    var gender = mutableStateOf("")
    var age = mutableStateOf("")
    var image = mutableStateOf("")
    var details = mutableStateOf("")
}