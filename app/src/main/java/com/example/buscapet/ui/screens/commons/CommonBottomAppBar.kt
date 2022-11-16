package com.example.buscapet.ui.screens.commons

import android.content.res.Configuration
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.buscapet.R
import com.example.buscapet.ui.navigation.NavItem
import com.example.buscapet.ui.theme.BuscaPetTheme

@Composable
fun CommonBottomAppBarM2(
    navController: NavController
) {
    val screens = listOf(
        NavItem.LastReportNavItem,
        NavItem.MyReportsNavItem
    )

    BuscaPetTheme {
        BottomAppBar(
            backgroundColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            cutoutShape = MaterialTheme.shapes.small.copy(
                CornerSize(percent = 50)
            )
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            screens.forEach { screen ->
                BottomNavigationItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_dog_paw),
                            contentDescription = screen.title,
                            tint = MaterialTheme.colorScheme.inverseSurface
                        )
                    },
                    label = {
                        Text(
                            text = screen.title,
                            fontSize = 9.sp,
                            color = MaterialTheme.colorScheme.inverseSurface
                        )
                    },
                    //selectedContentColor = MaterialTheme.colorScheme.inverseSurface,
                    //unselectedContentColor = MaterialTheme.colorScheme.inverseOnSurface,
                    alwaysShowLabel = true,
                    selected = currentRoute == screen.screenRoute,
                    onClick = {
                        navController.navigate(screen.screenRoute)
                        {
                            navController.graph.startDestinationRoute?.let { screen_route ->
                                popUpTo(screen_route) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun CommonBottomAppBarM3(
    navController: NavController
) {
    val screens = listOf(
        NavItem.LastReportNavItem,
        NavItem.MyReportsNavItem
    )

    BuscaPetTheme {
        BottomAppBar(
            backgroundColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            cutoutShape = MaterialTheme.shapes.extraSmall.copy(
                CornerSize(percent = 40)
            )
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            screens.forEach { screen ->
                BottomNavigationItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_dog_paw),
                            contentDescription = screen.title,
                            tint = MaterialTheme.colorScheme.inverseSurface
                        )
                    },
                    label = {
                        Text(
                            text = screen.title,
                            fontSize = 9.sp,
                            color = MaterialTheme.colorScheme.inverseSurface
                        )
                    },
                    //selectedContentColor = MaterialTheme.colorScheme.inverseSurface,
                    //unselectedContentColor = MaterialTheme.colorScheme.inverseOnSurface,
                    alwaysShowLabel = true,
                    selected = currentRoute == screen.screenRoute,
                    onClick = {
                        navController.navigate(screen.screenRoute)
                        {
                            navController.graph.startDestinationRoute?.let { screen_route ->
                                popUpTo(screen_route) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark View")
@Composable
fun PreviewCommonBottomAppBar() {
    CommonBottomAppBarM2(navController = rememberNavController())
}