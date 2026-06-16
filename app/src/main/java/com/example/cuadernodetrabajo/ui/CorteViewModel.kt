package com.example.cuadernodetrabajo.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.cuadernodetrabajo.model.Corte
import com.example.cuadernodetrabajo.repository.CorteRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.example.cuadernodetrabajo.network.RetrofitClient
import android.util.Log

class CorteViewModel(private val repository: CorteRepository) : ViewModel() {


    val allCortes: StateFlow<List<Corte>> = repository.allCortes.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun insert(corte: Corte) = viewModelScope.launch {
        repository.insert(corte)
    }

    fun delete(corte: Corte) = viewModelScope.launch {
        repository.delete(corte)
    }

    // Función para enviar los datos a la API simulada
    fun syncDataWithServer(onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                // Tomamos la lista actual de cortes guardados en Room
                val cortesActuales = allCortes.value

                if (cortesActuales.isNotEmpty()) {
                    // Enviamos los datos usando nuestro cliente Retrofit
                    val response = RetrofitClient.apiService.syncCortes(cortesActuales)

                    if (response.isSuccessful) {
                        Log.d("SYNC", "¡Datos sincronizados con éxito!")
                        onResult(true)
                    } else {
                        Log.e("SYNC", "Error del servidor: ${response.code()}")
                        onResult(false)
                    }
                } else {
                    // No hay datos para sincronizar
                    onResult(true)
                }
            } catch (e: Exception) {
                Log.e("SYNC", "Error de conexión: ${e.message}")
                onResult(false)
            }
        }
    }
}

// Esta clase "Factory" es necesaria para poder pasarle el Repositorio al ViewModel
// cuando la instanciemos en el MainActivity o NavHost
class CorteViewModelFactory(private val repository: CorteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CorteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CorteViewModel(repository) as T
        }
        throw IllegalArgumentException("Clase ViewModel desconocida")
    }
}