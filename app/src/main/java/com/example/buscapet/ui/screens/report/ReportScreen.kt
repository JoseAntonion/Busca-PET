package com.example.buscapet.ui.screens.report

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
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
import com.example.buscapet.ui.screens.commons.*
import com.google.firebase.auth.FirebaseAuth

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
    val userName = FirebaseAuth.getInstance().currentUser?.displayName
    MainView(
        user = userName,
        inputList = inputList
    )
}

@Composable
fun MainView(
    user: String?,
    inputList: List<Input>
) {
    CommonScaffoldM3(
        userName = user,
        content = { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    FormSections(inputList)
                }
            }
        },
        topAppBarIcon = Icons.Default.ArrowBack,
        topAppBarIconClick = {}
    )
}

@Composable
fun FormSections(inputList: List<Input>) {
    val viewModel: ReportViewModel = viewModel()
    for ((title, type, items) in inputList) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceAround
            ) {
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
                    InputType.Dropdown -> {
                        val breeds =
                            listOf("Policial", "Shaushau", "Haspapi", "Pudul", "Chico Terri")
                        CommonExposedDropdownMenuBox(breeds)
                    }
                }
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        CommonButton(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            text = "Reportar"
        )
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewReportScreen() {
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
    MainView(
        user = "Demo user",
        inputList = inputList
    )
}

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