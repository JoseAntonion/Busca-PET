package com.example.buscapet.ui.screens.report

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.buscapet.ui.model.FormData
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
    var formData by mutableStateOf(FormData())
    var showSnackbar by mutableStateOf(false)
    var snackbarText by mutableStateOf("")

    private fun toggleSnackbar(b: Boolean, text: String = "") {
        showSnackbar = b
        snackbarText = text
    }

    fun reportPet() {
        val prueba = hashMapOf(
            "color" to formData.color.value,
            "detalle" to formData.details.value,
            "edad" to formData.age.value,
            "genero" to formData.gender.value,
            "geo" to formData.geoLoc.value,
            "imagen" to formData.image.value,
            "raza" to formData.breed.value,
            "tamaño" to formData.size.value
        )

        Log.i(TAG, "reportPet: $prueba")

        Firebase.firestore
            .collection("mascotas")
            .document("My68dAGppMpxpnKTmYQ2")
            .collection("mascotas")
            .add(prueba)
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully written!")
                toggleSnackbar(true, "Success")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error writing document", e)
                toggleSnackbar(true, "${e.message}")
            }
    }
}