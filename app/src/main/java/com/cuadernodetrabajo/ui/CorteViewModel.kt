package com.cuadernodetrabajo.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.cuadernodetrabajo.model.Corte
import com.cuadernodetrabajo.repository.CorteRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.cuadernodetrabajo.network.RetrofitClient
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


    fun syncDataWithServer(onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val cortesActuales = allCortes.value

                if (cortesActuales.isNotEmpty()) {
                    val response = RetrofitClient.apiService.syncCortes(cortesActuales)

                    if (response.isSuccessful) {
                        Log.d("SYNC", "¡Datos sincronizados con éxito!")
                        onResult(true)
                    } else {
                        Log.e("SYNC", "Error del servidor: ${response.code()}")
                        onResult(false)
                    }
                } else {
                    onResult(true)
                }
            } catch (e: Exception) {
                Log.e("SYNC", "Error de conexión: ${e.message}")
                onResult(false)
            }
        }
    }
}


class CorteViewModelFactory(private val repository: CorteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CorteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CorteViewModel(repository) as T
        }
        throw IllegalArgumentException("Clase ViewModel desconocida")
    }
}