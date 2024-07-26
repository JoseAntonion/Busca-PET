package com.example.buscapet.add_pet.presentation

import android.content.res.Configuration
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.buscapet.R
import com.example.buscapet.core.domain.model.Pet
import com.example.buscapet.core.domain.model.PetState
import com.example.buscapet.core.presentation.AppBarWithBack
import com.example.buscapet.core.presentation.CommonOutlinedTextField
import com.example.buscapet.ui.theme.BuscaPetTheme
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

var petBirthInput: MutableState<String> = mutableStateOf("")
var petAgeInput: MutableState<String> = mutableStateOf("")
var petBreedInput: MutableState<String> = mutableStateOf("")
var petNameInput: MutableState<String> = mutableStateOf("")
var pickMedia: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>? = null

@Composable
fun AddPetScreen(
    navController: NavController = rememberNavController(),
    viewModel: AddPetViewModel = hiltViewModel(),
) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val currentUserName = currentUser?.displayName
    var petImage by remember { mutableStateOf<Uri?>(null) }
    val lContext = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val state by viewModel.state.collectAsState()

    pickMedia =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                petImage = uri
                Log.d("PhotoPicker", "Selected URI: $uri")
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

    LaunchedEffect(key1 = state.inserted) {
        if (state.inserted) {
            delay(2000L)
            Toast.makeText(lContext, "Reporte guardado", Toast.LENGTH_SHORT).show()
            navController.navigateUp()
        }
    }

    MainContainter(
        navController = navController,
        petImage = petImage,
        onSavePet = { pet ->
            if (pet.name?.isEmpty() == true ||
                pet.breed?.isEmpty() == true ||
                pet.age?.isEmpty() == true ||
                pet.birthday?.isEmpty() == true
            ) {
                Toast.makeText(lContext, "Debe llenar todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                coroutineScope.launch {
                    viewModel.savePet(pet)
                }
            }
        },
        currentUser = currentUserName,
        state = state
    )
}

@Composable
fun MainContainter(
    navController: NavController = rememberNavController(),
    petImage: Uri?,
    onSavePet: (Pet) -> Unit = {},
    currentUser: String?,
    state: AddPetViewModel.UiState,
) {
    BuscaPetTheme {
        Scaffold(
            topBar = {
                AppBarWithBack(
                    title = "Agregar nueva mascota",
                    onBackClick = { navController.navigateUp() }
                )
            }
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(it)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(14.dp)
                        .background(MaterialTheme.colorScheme.background)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    //if (petImage == null) {
                    Button(onClick = {
                        pickMedia?.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    }) {
                        Text(text = "Seleccione una imagen de tu mascota")
                    }
                    Spacer(modifier = Modifier.padding(8.dp))
                    //}
                    if (petImage != null) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                        ) {
                            Column {
                                AsyncImage(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight(Alignment.CenterVertically),
                                    model = petImage,
                                    placeholder = painterResource(R.drawable.dummy_puppy),
                                    contentDescription = "imagen de la mascota"
                                )
                                Box(
                                    modifier = Modifier
                                        .padding(14.dp)
                                ) {
                                    Column {
                                        petNameInput = remember { mutableStateOf("") }
                                        CommonOutlinedTextField(
                                            label = "Nombre",
                                            inputText = petNameInput,
                                            keyOption = KeyboardOptions(
                                                keyboardType = KeyboardType.Text,
                                                imeAction = ImeAction.Next
                                            )
                                        )
                                        Spacer(modifier = Modifier.padding(14.dp))
                                        petBreedInput = remember { mutableStateOf("") }
                                        CommonOutlinedTextField(
                                            label = "Raza",
                                            inputText = petBreedInput
                                        )
                                        Spacer(modifier = Modifier.padding(14.dp))
                                        petAgeInput = remember { mutableStateOf("") }
                                        CommonOutlinedTextField(
                                            label = "Edad",
                                            inputText = petAgeInput
                                        )
                                        Spacer(modifier = Modifier.padding(14.dp))
                                        petBirthInput = remember { mutableStateOf("") }
                                        CommonOutlinedTextField(
                                            label = "Fecha de nacimiento",
                                            inputText = petBirthInput
                                        )
                                        Spacer(modifier = Modifier.padding(14.dp))
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.padding(14.dp))
                        Button(
                            modifier = Modifier
                                .padding(bottom = 14.dp)
                                .align(Alignment.End),
                            onClick = {
                                onSavePet(
                                    Pet(
                                        name = petNameInput.value,
                                        breed = petBreedInput.value,
                                        age = petAgeInput.value,
                                        birthday = petBirthInput.value,
                                        owner = currentUser,
                                        petState = PetState.HOME
                                    )
                                )
                            }) {
                            if (state.loading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.width(35.dp),
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    trackColor = MaterialTheme.colorScheme.primary,
                                )
                            } else
                                androidx.compose.material.Text(
                                    text = "Guardar",
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                        }
                    }
                }
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "PreviewDARK")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "PreviewLIGHT")
@Composable
private fun PreviewAddPet() {
    MainContainter(
        petImage = "https://getdummyimage.com/200/200".toUri(),
        currentUser = "test user",
        state = AddPetViewModel.UiState(loading = false)
    )
}