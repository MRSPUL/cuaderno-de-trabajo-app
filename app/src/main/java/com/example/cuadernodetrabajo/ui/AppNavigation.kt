package com.example.cuadernodetrabajo.ui

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cuadernodetrabajo.repository.CorteRepository
import com.example.cuadernodetrabajo.ui.detail.NuevoCorteScreen
import com.example.cuadernodetrabajo.ui.home.HomeScreen
import com.example.cuadernodetrabajo.ui.onboarding.OnboardingScreen
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.cuadernodetrabajo.ui.detail.DetalleCorteScreen
import android.content.SharedPreferences

@Composable
fun AppNavigation(
    repository: CorteRepository,
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
            val viewModel: CorteViewModel = viewModel(factory = CorteViewModelFactory(repository))
            val listaCortes by viewModel.allCortes.collectAsState()

            HomeScreen(
                cortes = listaCortes,
                onNavigateToNewCorte = { navController.navigate("nuevo_corte") },
                onNavigateToDetalle = { id -> navController.navigate("detalle_corte/$id") },
                onSyncClick = {
                    Toast.makeText(context, "Sincronizando...", Toast.LENGTH_SHORT).show()
                    viewModel.syncDataWithServer { success ->
                        val mensaje = if (success) "¡Sincronización exitosa!" else "Error al sincronizar"
                        Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }

        composable("nuevo_corte") {
            val viewModel: CorteViewModel = viewModel(
                factory = CorteViewModelFactory(repository)
            )


            NuevoCorteScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = "detalle_corte/{corteId}",
            arguments = listOf(navArgument("corteId") { type = NavType.IntType })
        ) { backStackEntry ->
            val corteId = backStackEntry.arguments?.getInt("corteId") ?: 0
            val viewModel: CorteViewModel = viewModel(factory = CorteViewModelFactory(repository))

            DetalleCorteScreen(
                corteId = corteId,
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}