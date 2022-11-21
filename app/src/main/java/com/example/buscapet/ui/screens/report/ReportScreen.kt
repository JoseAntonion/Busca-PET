package com.example.buscapet.ui.screens.report

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.buscapet.ui.screens.commons.CommonTextField

@Composable
fun ReportScreen(
    navController: NavHostController
) {
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

    val viewModel: ReportViewModel = viewModel()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxHeight()
                .padding(16.dp)
        ) {
            CommonTextField(
                hintText = "color",
                data = viewModel.formData.color,
                imeAction = ImeAction.Next
            )
            CommonTextField(
                hintText = "tamaño",
                data = viewModel.formData.size,
                imeAction = ImeAction.Next
            )
            CommonTextField(
                hintText = "Raza",
                data = viewModel.formData.breed,
                imeAction = ImeAction.Next
            )
            CommonTextField(
                hintText = "GeoLocalizacion",
                data = viewModel.formData.geoLoc,
                imeAction = ImeAction.Next
            )
            CommonTextField(
                hintText = "Genero",
                data = viewModel.formData.gender,
                imeAction = ImeAction.Next
            )
            CommonTextField(
                hintText = "Edad",
                data = viewModel.formData.age,
                imeAction = ImeAction.Next
            )
            CommonTextField(
                hintText = "Image",
                data = viewModel.formData.image,
                imeAction = ImeAction.Next
            )
            CommonTextField(
                hintText = "Detalles",
                data = viewModel.formData.details,
                imeAction = ImeAction.Done
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewReportScreen() {
    ReportScreen(rememberNavController())
}