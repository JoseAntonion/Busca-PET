package com.example.buscapet.ui.screens.report

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ReportViewModel : ViewModel() {

    /**
     * color (TextField pormientras)
     * tamaño (Spinner o cards con imagen referencial)
     * raza (Spinner)
     * GeoLoc (Auto y/o Manual)
     * Genero (RadioButton)
     * Edad (RadioButton - cachorro,adulto,senior)
     * Imagen (Adjuntar imagen)
     * Datos adicionales (TextField grande para escribir)
     */

    private val _color = MutableLiveData<String>()
    val color: LiveData<String>
        get() = _color
    private val _size = MutableLiveData<String>()
    val size: LiveData<String>
        get() = _size
    private val _breed = MutableLiveData<String>()
    val breed: LiveData<String>
        get() = _breed
    private val _geoLoc = MutableLiveData<String>()
    val geoLoc: LiveData<String>
        get() = _geoLoc
    private val _gender = MutableLiveData<String>()
    val gender: LiveData<String>
        get() = _gender
    private val _age = MutableLiveData<String>()
    val age: LiveData<String>
        get() = _age
    private val _image = MutableLiveData<String>()
    val image: LiveData<String>
        get() = _image
    private val _details = MutableLiveData<String>()
    val details: LiveData<String>
        get() = _details

    val formData = FormData()

    class FormData {
        val color = mutableStateOf("")
        val size = mutableStateOf("")
        val breed = mutableStateOf("")
        val geoLoc = mutableStateOf("")
        val gender = mutableStateOf("")
        val age = mutableStateOf("")
        val image = mutableStateOf("")
        val details = mutableStateOf("")
    }

}