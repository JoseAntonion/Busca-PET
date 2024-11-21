package com.example.buscapet.home.presentation

import android.content.res.Configuration
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.FabPosition
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
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
import com.example.buscapet.last_resports.presentation.LastReportsScreen
import com.example.buscapet.my_pets.presentation.MyPetsScreen
import com.example.buscapet.my_reports.presentation.MyReportsScreen

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val currentContext = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val photo = uiState.currentUser?.photoUrl
    val nameSplited = uiState.currentUser?.displayName?.split(" ")
    val displayName = nameSplited
        ?.filterIndexed { index, _ -> index % 2 == 0 }
        ?.joinToString(" ") {
            it.capitalize(Locale.current)
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
        displayName = displayName,
        navController = navController,
        profilePhoto = photo,
        tabItems = tabItems,
        onReportClick = { viewModel.showReportDialog() },
        onLostMyOwnClick = {
            if (uiState.myPets.isEmpty()) {
                Toast.makeText(
                    currentContext,
                    "Primero debes agregar alguna mascota",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    currentContext,
                    "Implementar funcionalidad con mascotas ya agregadas",
                    Toast.LENGTH_SHORT
                ).show()
            }
        },
        onReportLostClick = { navController.navigate(Report) },
        onReportDialogDismiss = { viewModel.dismissReportDialog() }
    )
}

@Composable
fun HomeContainer(
    navController: NavController = rememberNavController(),
    reportDialog: Boolean = false,
    displayName: String?,
    profilePhoto: Uri? = null,
    tabItems: List<TabItem> = listOf(),
    onReportClick: () -> Unit = {},
    onLostMyOwnClick: () -> Unit = {},
    onReportLostClick: () -> Unit = {},
    onReportDialogDismiss: () -> Unit = {},
) {
    val pagerState = rememberPagerState(0, 0f) { tabItems.size }
    val scope = rememberCoroutineScope()

    if (reportDialog) {
        ReportAlertDialog(
            onDismiss = { onReportDialogDismiss() },
            onReportLostClick = { onReportLostClick() },
            onLostMyOwnClick = { onLostMyOwnClick() }
        )
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                CommonTopAppBar(
                    userName = displayName,
                    photo = profilePhoto,
                    onIconClick = {
                        navController.navigate(Profile)
                    },
                    onMenuClick = {

                    }
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
    HomeContainer(
        displayName = "John Johnson"
    )
}