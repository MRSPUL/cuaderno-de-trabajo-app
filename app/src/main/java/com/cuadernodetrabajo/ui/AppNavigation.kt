package com.cuadernodetrabajo.ui
import android.content.SharedPreferences
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cuadernodetrabajo.ui.detail.NuevoCorteScreen
import com.cuadernodetrabajo.ui.home.HomeScreen
import com.cuadernodetrabajo.ui.onboarding.OnboardingScreen
import com.cuadernodetrabajo.ui.detail.DetalleCorteScreen

@Composable
fun AppNavigation(
    viewModel: CorteViewModel,
    startDestination: String,
    sharedPreferences: SharedPreferences
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {

        composable("onboarding") {
            OnboardingScreen(
                onFinish = {
                    sharedPreferences.edit().putBoolean("ONBOARDING_OK", true).apply()
                    navController.navigate("home") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                }
            )
        }


        composable("home") {
            val context = LocalContext.current


            val listaCortes = viewModel.listaCortes

            HomeScreen(
                cortes = listaCortes,
                onNavigateToNewCorte = { navController.navigate("nuevo_corte") },
                onNavigateToDetalle = { id -> navController.navigate("detalle_corte/$id") },

            )
        }


        composable("nuevo_corte") {
            NuevoCorteScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }


        composable(
            route = "detalle_corte/{corteId}",
            arguments = listOf(navArgument("corteId") { type = NavType.StringType })
        ) { backStackEntry ->
            val corteId = backStackEntry.arguments?.getString("corteId") ?: ""

            DetalleCorteScreen(
                corteId = corteId,
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}