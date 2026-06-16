package com.example.cuadernodetrabajo.repository

import com.example.cuadernodetrabajo.data.CorteDao
import com.example.cuadernodetrabajo.model.Corte
import kotlinx.coroutines.flow.Flow

class CorteRepository(private val corteDao: CorteDao) {

    // Obtenemos todos los cortes directamente del DAO
    val allCortes: Flow<List<Corte>> = corteDao.getAllCortes()

    // Funciones suspend para no bloquear la pantalla principal
    suspend fun insert(corte: Corte) {
        corteDao.insertCorte(corte)
    }

    suspend fun delete(corte: Corte) {
        corteDao.deleteCorte(corte)
    }

    suspend fun getCorteById(id: Int): Corte? {
        return corteDao.getCorteById(id)
    }
}