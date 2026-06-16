package com.example.cuadernodetrabajo.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cortes")
data class Corte(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val marca: String,
    val numeroCorte: String,
    val cantidadCamisas: Int,
    val datosAdicionales: String,
    val photoUri: String? = null,
    val fechaCreacion: Long = System.currentTimeMillis()
)