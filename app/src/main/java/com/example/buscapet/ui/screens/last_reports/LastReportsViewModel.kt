package com.example.buscapet.ui.screens.last_reports

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LastReportsViewModel : ViewModel() {

    private val _showDialog = MutableLiveData<Boolean>()
    val showDialog: LiveData<Boolean>
        get() = _showDialog
// Comentario de prueba
    val prueba = "hola prueba desde dell"
    fun dialogState(b: Boolean) {
        _showDialog.value = b
    }
}