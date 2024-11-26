package com.example.buscapet.home.presentation

import android.app.Activity
import android.content.res.Configuration
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.buscapet.R
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
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val currentContext = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var openDialog by remember { mutableStateOf(false) }

    BackHandler(enabled = true) {
        (currentContext as Activity).finish()
    }

    when {
        openDialog -> {
            ReportAlertDialog(
                onDismiss = { openDialog = false },
                onReportLostClick = { navController.navigate(Report) },
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
                }
            )
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
) {
    val pagerState = rememberPagerState(0, 0f) { tabItems.size }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                CommonTopAppBar(
                    userName = uiState.currentUser ?: "",
                    photo = uiState.photo,
                    onIconClick = { onProfileClick() },
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
    HomeContainer(
        uiState = HomeState(currentUser = "Usuario Preview"),
        navController = rememberNavController(),
    )
}