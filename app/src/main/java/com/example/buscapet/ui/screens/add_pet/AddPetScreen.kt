package com.example.buscapet.ui.screens.add_pet

import android.content.res.Configuration
import android.net.Uri
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.buscapet.R
import com.example.buscapet.ui.commons.AppBarWithBack
import com.example.buscapet.ui.commons.CommonFilledTextField
import com.example.buscapet.ui.theme.BuscaPetTheme

var pickMedia: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>? = null

@Composable
fun AddPetScreen(navController: NavController = rememberNavController()) {
    var petImage by remember { mutableStateOf<Uri?>(null) }
    pickMedia =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            petImage = uri
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: $uri")
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }
    MainContainter(
        navController = navController,
        petImage = petImage
    )
}

@Composable
fun MainContainter(
    navController: NavController = rememberNavController(),
    petImage: Uri?
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
                        .background(MaterialTheme.colorScheme.background),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(onClick = {
                        pickMedia?.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    }) {
                        Text(text = "Seleccione una imagen de tu mascota")
                    }
                    Spacer(modifier = Modifier.padding(14.dp))
                    AsyncImage(
                        model = petImage,
                        placeholder = painterResource(R.drawable.dummy_puppy),
                        contentDescription = "imagen de la mascota"
                    )
                    val petNameInput = remember { mutableStateOf("") }
                    CommonFilledTextField(
                        label = "Nombre de la mascota",
                        inputText = petNameInput
                    )
                    Spacer(modifier = Modifier.padding(14.dp))
                    Button(onClick = { /*TODO*/ }) {
                        Text(text = "Guardar")
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
    MainContainter(petImage = "https://getdummyimage.com/200/200".toUri())
}