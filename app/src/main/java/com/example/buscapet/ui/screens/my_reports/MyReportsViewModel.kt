package com.example.buscapet.ui.screens.my_reports

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyReportsViewModel : ViewModel() {

    private val _showDialog = MutableLiveData<Boolean>()
    val showDialog: LiveData<Boolean>
        get() = _showDialog

    fun dialogState(b: Boolean) {
        _showDialog.value = b
    }

}