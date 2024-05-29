package com.example.buscapet.ui.screens.last_reports

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.buscapet.domain.model.Pet
import com.example.buscapet.ui.commons.CommonCardView
import com.example.buscapet.ui.navigation.BottomNavScreens
import com.example.buscapet.ui.theme.BuscaPetTheme
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LastReportsScreen(
    navController: NavHostController
) {
    val userName = FirebaseAuth.getInstance().currentUser?.displayName
    val petList = listOf(
        Pet(1, "Neru", 5, "perro", null, cover = "se perdio la neru"),
        Pet(2, "Fuki", 3, "perro", null, cover = "se perdio la kuki"),
        Pet(3, "Jota", 2, "perro", null, cover = "se perdio la jota"),
        Pet(4, "Crepu", 1, "gato", null, cover = "se perdio la prepu"),
        Pet(5, "Mita", 2, "gato", null, cover = "se perdio la mita"),
        Pet(6, "Mita", 2, "gato", null, cover = "se perdio la mita"),
        Pet(7, "Mita", 2, "gato", null, cover = "se perdio la mita"),
        Pet(8, "Mita", 2, "gato", null, cover = "se perdio la mita"),
        Pet(9, "Mita", 2, "gato", null, cover = "se perdio la mita"),
        Pet(10, "Mita", 2, "gato", null, cover = "se perdio la mita"),
        Pet(11, "Mita", 2, "gato", null, cover = "se perdio la mita"),
        Pet(12, "Mita", 2, "gato", null, cover = "se perdio la mita"),
        Pet(13, "Mita", 2, "gato", null, cover = "se perdio la mita"),
        Pet(14, "Mita", 2, "gato", null, cover = "se perdio la mita"),
    )
    MainView(
        userName = userName,
        petList = petList,
        navController = navController
    )
}

@Composable
fun MainView(
    userName: String?,
    petList: List<Pet> = emptyList(),
    navController: NavHostController
) {
    BuscaPetTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(petList) {
                    CommonCardView(
                        modifier = Modifier
                            .padding(vertical = 8.dp),
                        title = it.nombre,
                        subtitle = it.cover ?: "no cover",
                        onClick = {
                            navController.navigate(BottomNavScreens.DetailReport.route)
                        }
                        //testUri = Uri.parse("https://getdummyimage.com/300/300")
                    )
                }
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "PreviewDARK")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "PreviewLIGHT")
@Composable
fun PreviewDark() {
    val petList = listOf(
        Pet(1, "Neru", 5, "perro", null, cover = "se perdio la neru"),
        Pet(2, "Fuki", 3, "perro", null, cover = "se perdio la kuki"),
        Pet(3, "Jota", 2, "perro", null, cover = "se perdio la jota"),
        Pet(4, "Crepu", 1, "gato", null, cover = "se perdio la prepu"),
        Pet(5, "Mita", 2, "gatp", null, cover = "se perdio la mita")
    )
    MainView(
        userName = "Usuario prueba",
        petList = petList,
        rememberNavController()
    )
}