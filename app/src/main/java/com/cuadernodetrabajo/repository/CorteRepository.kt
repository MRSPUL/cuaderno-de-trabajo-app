package com.cuadernodetrabajo.repository

import com.cuadernodetrabajo.model.Corte
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class CorteRepository {

    private val db = FirebaseFirestore.getInstance()
    private val coleccion = db.collection("CortesTaller")

    fun guardarCorte(corte: Corte) {
        coleccion.add(corte)
    }

    fun actualizarCorte(corte: Corte) {
        coleccion.document(corte.id).set(corte)
    }

    fun eliminarCorte(id: String) {
        coleccion.document(id).delete()
    }

    fun obtenerCortesEnTiempoReal(onActualizacion: (List<Corte>) -> Unit) {
        coleccion.orderBy("fechaCreacion", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) {
                    return@addSnapshotListener
                }

                val lista = snapshot.documents.mapNotNull { documento ->
                    val corte = documento.toObject(Corte::class.java)
                    corte?.copy(id = documento.id)
                }

                onActualizacion(lista)
            }
    }
}