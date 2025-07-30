package ru.mokolomyagi.photofactcheck.data.model

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.mokolomyagi.photofactcheck.data.network.ApiService

object RetrofitClient {
    private const val BASE_URL = "https://mobdev.kolomyagiquest.ru/api/v1/"

    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiService::class.java)
    }
}