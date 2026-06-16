package com.example.cuadernodetrabajo.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cuadernodetrabajo.model.Corte
import kotlinx.coroutines.flow.Flow

@Dao
interface CorteDao {
    // Inserta un nuevo corte o lo reemplaza si ya existe
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCorte(corte: Corte)

    // Obtiene todos los cortes ordenados por fecha (el más nuevo primero)
    // Usamos Flow para que la UI se actualice automáticamente si hay cambios
    @Query("SELECT * FROM cortes ORDER BY fechaCreacion DESC")
    fun getAllCortes(): Flow<List<Corte>>

    // Obtiene un solo corte por su ID (útil para la pantalla de detalle)
    @Query("SELECT * FROM cortes WHERE id = :id")
    suspend fun getCorteById(id: Int): Corte?

    // Elimina un corte
    @Delete
    suspend fun deleteCorte(corte: Corte)
}