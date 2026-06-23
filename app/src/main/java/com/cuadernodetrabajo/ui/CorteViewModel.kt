package com.cuadernodetrabajo.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.cuadernodetrabajo.repository.CorteRepository
import com.cuadernodetrabajo.model.Corte

class CorteViewModel : ViewModel() {

    private val repository = CorteRepository()

    var listaCortes = mutableStateListOf<Corte>()
        private set


    init {
        repository.obtenerCortesEnTiempoReal { listaActualizada ->

            val listaOrdenada = listaActualizada.sortedByDescending { it.fechaCreacion }

            listaCortes.clear()
            listaCortes.addAll(listaOrdenada)
        }
    }

    fun insert(nuevoCorte: Corte) {
        repository.guardarCorte(nuevoCorte)
    }

    fun actualizar(corte: Corte) {
        repository.actualizarCorte(corte)
    }

    fun eliminar(id: String) {
        repository.eliminarCorte(id)
    }
}