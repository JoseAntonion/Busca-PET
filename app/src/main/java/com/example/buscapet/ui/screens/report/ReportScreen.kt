package com.example.buscapet.ui.screens.report

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.buscapet.ui.screens.commons.ChipGroup
import com.example.buscapet.ui.screens.commons.CommonTextField
import com.example.buscapet.ui.theme.BuscaPetTheme

enum class InputType {
    TextField,
    Dropdown,
    ChipList,
    Map,
    RadioButton,
}

data class Input(
    val title: String,
    val type: InputType,
    val items: List<String> = emptyList()
)

@Composable
fun ReportScreen(
    navController: NavHostController
) {
    /**
     * color (TextField pormientras)
     * tamaño (Spinner o cards con imagen referencial)
     * raza (Spinner)
     * GeoLoc (Auto y/o Manual)
     * Genero (ChipGroup)
     * Edad (ChipGroup - cachorro,adulto,senior)
     * Imagen (Adjuntar imagen)
     * Datos adicionales (TextField grande para escribir)
     */

    val inputList = listOf(
        Input("Color", InputType.TextField),
        Input("Tamaño", InputType.ChipList, listOf("Pequeño", "Mediano", "Grande", "Gigante")),
        Input("Raza", InputType.Dropdown),
        Input("Ubicacion", InputType.Map),
        Input("Genero", InputType.ChipList, listOf("Hembra", "Macho")),
        Input("Edad", InputType.ChipList, listOf("Cachorro", "Adulto", "Senior")),
        Input("Imagen", InputType.TextField),
        Input("Detalles", InputType.TextField)
    )

    BuscaPetTheme {
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
                FormSections(inputList)
            }
        }
    }
}

@Composable
fun FormSections(inputList: List<Input>) {
    val viewModel: ReportViewModel = viewModel()
    for ((title, type, items) in inputList) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column {
                when (type) {
                    InputType.ChipList -> {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        ChipGroup(items = items)
                    }
                    InputType.TextField -> {
                        val text = remember { mutableStateOf("") }
                        CommonTextField(
                            hintText = title,
                            data = text,
                            //imeAction = ImeAction.Next
                        )
                    }
                    InputType.RadioButton -> {}
                    InputType.Map -> {}
                    InputType.Dropdown -> {}
                }
            }
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewReportScreen() {
    ReportScreen(rememberNavController())
}