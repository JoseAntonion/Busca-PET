package com.example.buscapet.home.presentation

import android.app.Activity
import android.content.res.Configuration
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.buscapet.R
import com.example.buscapet.core.navigation.Profile
import com.example.buscapet.core.navigation.Report
import com.example.buscapet.core.presentation.CommonFloatingActionButton
import com.example.buscapet.core.presentation.CommonTabBar
import com.example.buscapet.core.presentation.CommonTopAppBar
import com.example.buscapet.core.presentation.ReportAlertDialog
import com.example.buscapet.core.presentation.TabItem
import com.example.buscapet.core.presentation.util.ObserveAsEvents
import com.example.buscapet.last_resports.presentation.LastReportsScreen
import com.example.buscapet.my_pets.presentation.MyPetsScreen
import com.example.buscapet.my_reports.presentation.MyReportsScreen
import com.example.buscapet.ui.theme.BuscaPetTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val currentContext = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var openDialog by remember { mutableStateOf(false) }
    var showPetSelectionSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val snackbarHostState = remember { SnackbarHostState() }

    BackHandler(enabled = true) {
        (currentContext as Activity).finish()
    }

    // Logic to receive messages from other screens (Report/Detail)
    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    val successMessage = savedStateHandle?.get<String>("user_message")

    LaunchedEffect(successMessage) {
        if (!successMessage.isNullOrEmpty()) {
            snackbarHostState.showSnackbar(
                message = successMessage,
                duration = SnackbarDuration.Short
            )
            savedStateHandle["user_message"] = ""
        }
    }

    // Observe global snackbars from ViewModel
    ObserveAsEvents(
        flow = viewModel.homeEvents,
        snackbarHostState = snackbarHostState
    )

    if (openDialog) {
        ReportAlertDialog(
            onDismiss = { openDialog = false },
            onReportLostClick = {
                openDialog = false
                navController.navigate(Report())
            },
            onLostMyOwnClick = {
                openDialog = false
                if (uiState.myPets.isEmpty()) {
                    Toast.makeText(
                        currentContext,
                        "Primero debes agregar alguna mascota",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (uiState.myPets.size == 1) {
                    viewModel.onReportExistingPet(uiState.myPets.first())
                } else {
                    showPetSelectionSheet = true
                }
            }
        )
    }

    if (showPetSelectionSheet) {
        ModalBottomSheet(
            onDismissRequest = { showPetSelectionSheet = false },
            sheetState = sheetState
        ) {
            LazyColumn(
                modifier = Modifier.padding(16.dp)
            ) {
                item {
                    Text(
                        text = "Selecciona una mascota",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
                items(uiState.myPets) { pet ->
                    ListItem(
                        headlineContent = { Text(pet.name ?: "Sin nombre") },
                        supportingContent = { Text(pet.breed ?: "") },
                        modifier = Modifier.clickable {
                            showPetSelectionSheet = false
                            viewModel.onReportExistingPet(pet)
                        }
                    )
                }
            }
        }
    }

    val tabItems = listOf(
        TabItem(
            title = "Ultimos reportes",
            icon = ImageVector.vectorResource(id = R.drawable.ic_dog_paw)
        ),
        TabItem(
            title = "Mis reportes",
            icon = ImageVector.vectorResource(id = R.drawable.ic_dog)
        ),
        TabItem(
            title = "Mis Mascotas",
            icon = ImageVector.vectorResource(id = R.drawable.ic_my_pets)
        )
    )

    HomeContainer(
        uiState = uiState,
        navController = navController,
        tabItems = tabItems,
        onReportClick = { openDialog = true },
        onProfileClick = { navController.navigate(Profile)},
        snackbarHostState = snackbarHostState
    )
}

@Composable
fun HomeContainer(
    uiState: HomeState,
    navController: NavController,
    tabItems: List<TabItem> = listOf(),
    onReportClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    snackbarHostState: SnackbarHostState
) {
    val pagerState = rememberPagerState(0, 0f) { tabItems.size }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                CommonTopAppBar(
                    userName = uiState.currentUser ?: "",
                    photo = uiState.photo,
                    onAccountClick = { onProfileClick() },
                    onMenuClick = { onMenuClick() }
                )
                CommonTabBar(
                    pagerState = pagerState,
                    scope = scope,
                    tabItems = tabItems
                )
            }
        },
        floatingActionButton = {
            CommonFloatingActionButton {
                onReportClick()
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
    ) { paddingValues ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ) { index ->
            when (index) {
                0 -> LastReportsScreen(navController = navController)
                1 -> MyReportsScreen(navController = navController)
                2 -> MyPetsScreen(navController = navController)
            }
        }
    }
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "PreviewDARK")
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "PreviewLIGHT")
@Composable
fun PreviewMainView() {
    BuscaPetTheme {
        HomeContainer(
            uiState = HomeState(currentUser = "Usuario Preview"),
            navController = rememberNavController(),
            snackbarHostState = SnackbarHostState()
        )
    }
}