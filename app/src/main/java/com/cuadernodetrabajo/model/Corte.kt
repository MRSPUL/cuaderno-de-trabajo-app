package com.cuadernodetrabajo.model

data class Corte(
    val id: String = "",
    val marca: String = "",
    val numeroCorte: String = "",
    val cantidadCamisas: Int = 0,
    val datosAdicionales: String = "",
    val photoUri: String = "",
    val fechaCreacion: Long = System.currentTimeMillis()
)