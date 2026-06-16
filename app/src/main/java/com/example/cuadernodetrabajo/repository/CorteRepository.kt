package com.example.cuadernodetrabajo.repository

import com.example.cuadernodetrabajo.data.CorteDao
import com.example.cuadernodetrabajo.model.Corte
import kotlinx.coroutines.flow.Flow

class CorteRepository(private val corteDao: CorteDao) {

    val allCortes: Flow<List<Corte>> = corteDao.getAllCortes()

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