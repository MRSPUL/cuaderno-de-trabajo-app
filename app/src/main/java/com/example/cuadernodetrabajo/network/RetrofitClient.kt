package com.example.cuadernodetrabajo.network

import com.example.cuadernodetrabajo.model.Corte
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

// 1. Definimos las rutas de nuestra API
interface ApiService {
    // Usamos un endpoint público de prueba para simular que guardamos los datos en la nube
    @POST("posts")
    suspend fun syncCortes(@Body cortes: List<Corte>): retrofit2.Response<Any>
}

// 2. Construimos el cliente
object RetrofitClient {
    // URL base de nuestra API simulada
    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            // Gson se encarga de convertir nuestra lista de datos Kotlin a JSON automáticamente
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}