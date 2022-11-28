package com.example.buscapet.ui.screens.report

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

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

    val TAG = ReportViewModel::class.java.simpleName

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

    fun reportPet() {
        val prueba = hashMapOf(
            "color" to "Pruea",
            "detalle" to "Pruea",
            "edad" to "Pruea",
            "genero" to "Pruea",
            "geo" to "Pruea",
            "imagen" to "Pruea",
            "raza" to "Pruea",
            "tamaño" to "Pruea"
        )

        Firebase.firestore
            .collection("mascotas")
            .document("My68dAGppMpxpnKTmYQ2")
            .collection("mascotas")
            .add(prueba)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }

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