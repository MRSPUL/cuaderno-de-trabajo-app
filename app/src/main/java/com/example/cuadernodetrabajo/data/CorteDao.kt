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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCorte(corte: Corte)


    @Query("SELECT * FROM cortes ORDER BY fechaCreacion DESC")
    fun getAllCortes(): Flow<List<Corte>>


    @Query("SELECT * FROM cortes WHERE id = :id")
    suspend fun getCorteById(id: Int): Corte?

    @Delete
    suspend fun deleteCorte(corte: Corte)
}