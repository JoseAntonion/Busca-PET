package com.example.buscapet.ui.screens.report

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.buscapet.ui.model.FormData
import com.example.buscapet.ui.screens.commons.*
import com.example.buscapet.ui.theme.BuscaPetTheme
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ReportScreen(
    navController: NavHostController
) {
    val userName = FirebaseAuth.getInstance().currentUser?.displayName
    BackHandler { navController.popBackStack() }
    MainView(
        navController,
        user = userName,
    )
}

@Composable
fun MainView(
    navController: NavHostController,
    user: String?,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    BuscaPetTheme {
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
                        FormSections(
                            snackbarHostState = snackbarHostState,
                            scope = scope
                        )
                    }
                }
            },
            topAppBarIcon = Icons.Default.ArrowBack,
            topAppBarIconClick = { navController.popBackStack() },
            snackbarHostState = { SnackbarHost(snackbarHostState) }
        )
    }
}

@Composable
fun FormSections(
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope
) {
    val viewModel: ReportViewModel = viewModel()
    val formBody = FormData()

    // Color
    InputContainer {
        CommonTextField(
            hintText = "Color",
            data = formBody.color,
            imeAction = ImeAction.Next
        )
    }
    // Tamaño
    InputContainer {
        val sizes = listOf("Pequeño", "Mediano", "Grande", "Gigante")
        Text(
            text = "Tamaño",
            //style = MaterialTheme.typography.titleMedium,
            //color = MaterialTheme.colorScheme.onBackground
        )
        ChipGroup(
            items = sizes,
            formBody.size
        )
    }
    // Raza
    InputContainer {
        val breeds =
            listOf("Policial", "Shaushau", "Haspapi", "Pudul", "Chico Terri")
        CommonExposedDropdownMenuBox(
            hintText = "Raza",
            breeds = breeds,
            formBody.breed
        )
    }
    // Genero
    InputContainer {
        val genders = listOf("Hembra", "Macho")
        Text(
            text = "Genero",
            //style = MaterialTheme.typography.titleMedium,
            //color = MaterialTheme.colorScheme.onBackground
        )
        ChipGroup(
            items = genders,
            formBody.gender
        )
    }
    // Edad
    InputContainer {
        val ages = listOf("Cachorro", "Adulto", "Senior")
        Text(
            text = "Edad",
            //style = MaterialTheme.typography.titleMedium,
            //color = MaterialTheme.colorScheme.onBackground
        )
        ChipGroup(
            items = ages,
            formBody.age
        )
    }
    // Imagen
    InputContainer {
        CommonTextField(
            hintText = "Imagen",
            data = formBody.image,
            imeAction = ImeAction.Next
        )
    }
    // Detalle
    InputContainer {
        CommonTextField(
            hintText = "Detalle",
            data = formBody.details,
            imeAction = ImeAction.Done
        )
    }

    /*for ((title, type, items) in inputList) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceAround
            ) {
                when (type) {
                    InputType.ChipList -> {
                        var chipState by remember { mutableStateOf("Pequeño") }
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        ChipGroup(
                            items = items,
                            chipState
                        ) {
                            chipState = if (it != chipState) it else ""
                            when (title) {
                                "Tamaño" -> { formBody.size = chipState }
                                "Genero" -> {formBody.gender = chipState}
                                "Edad" -> {formBody.age = chipState}
                            }
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    "Snackbar # $chipState"
                                )
                            }
                        }
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
    }*/

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        CommonButton(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            text = "Reportar",
            onClick = {
                viewModel.formData = formBody
                viewModel.reportPet()
            }
        )
    }
    LaunchedEffect(viewModel.showSnackbar) {
        scope.launch {
            snackbarHostState.showSnackbar(
                message = viewModel.snackbarText
            )
        }
    }
    //ShowSnackbar()

}

@Composable
fun InputContainer(
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceAround
        ) {
            content()
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewReportScreen() {
//    val inputList = listOf(
//        Input("Color", InputType.TextField),
//        Input("Tamaño", InputType.ChipList, listOf("Pequeño", "Mediano", "Grande", "Gigante")),
//        Input("Raza", InputType.Dropdown),
//        Input("Ubicacion", InputType.Map),
//        Input("Genero", InputType.ChipList, listOf("Hembra", "Macho")),
//        Input("Edad", InputType.ChipList, listOf("Cachorro", "Adulto", "Senior")),
//        Input("Imagen", InputType.TextField),
//        Input("Detalles", InputType.TextField)
//    )
    MainView(
        user = "Demo user",
        navController = rememberNavController()
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