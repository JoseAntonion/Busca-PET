package com.example.buscapet.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.buscapet.auth.presentation.sign_in.SignInScreen
import com.example.buscapet.add_pet.presentation.AddPetScreen
import com.example.buscapet.detail_report.presentation.DetailReportScreen
import com.example.buscapet.home.presentation.HomeScreen
import com.example.buscapet.profile.presentation.ProfileScreen
import com.example.buscapet.report.presentation.ReportScreen

@Composable
fun RootNavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = SignIn
    ) {
        composable<SignIn> { SignInScreen(navController = navController) }
        composable<Home> { HomeScreen(navController = navController) }
        composable<Report> { ReportScreen(navController = navController) }
        composable<AddPet> { AddPetScreen(navController = navController) }
        composable<Profile> { ProfileScreen(navController = navController) }
        composable<DetailReport> { backStackEntry ->
            val pet = backStackEntry.toRoute<DetailReport>()
            DetailReportScreen(
                navController = navController,
                petId = pet.id
            )
        }
    }
}