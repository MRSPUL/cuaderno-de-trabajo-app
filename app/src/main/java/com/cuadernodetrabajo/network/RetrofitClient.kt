package com.cuadernodetrabajo.network

import com.cuadernodetrabajo.model.Corte
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST


interface ApiService {

    @POST("posts")
    suspend fun syncCortes(@Body cortes: List<Corte>): retrofit2.Response<Any>
}


object RetrofitClient {

    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)

            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}