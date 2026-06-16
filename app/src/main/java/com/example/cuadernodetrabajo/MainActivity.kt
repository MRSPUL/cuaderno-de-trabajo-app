package com.example.cuadernodetrabajo

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.cuadernodetrabajo.data.AppDatabase
import com.example.cuadernodetrabajo.repository.CorteRepository
import com.example.cuadernodetrabajo.ui.AppNavigation
import com.example.cuadernodetrabajo.ui.theme.CuadernoDeTrabajoTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getDatabase(this)
        val repository = CorteRepository(database.corteDao())


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
                        repository = repository,
                        startDestination = rutaInicial,
                        sharedPreferences = sharedPreferences
                    )
                }
            }
        }
    }
}