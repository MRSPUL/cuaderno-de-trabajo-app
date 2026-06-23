package com.cuadernodetrabajo

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.cuadernodetrabajo.ui.AppNavigation
import com.cuadernodetrabajo.ui.CorteViewModel
import com.cuadernodetrabajo.ui.theme.CuadernoDeTrabajoTheme

class MainActivity : ComponentActivity() {
    private val viewModel: CorteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE)
        val onboardingCompletado = sharedPreferences.getBoolean("ONBOARDING_OK", false)
        val rutaInicial = if (onboardingCompletado) "home" else "onboarding"

        setContent {
            CuadernoDeTrabajoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(
                        viewModel = viewModel,
                        startDestination = rutaInicial,
                        sharedPreferences = sharedPreferences
                    )
                }
            }
        }
    }
}