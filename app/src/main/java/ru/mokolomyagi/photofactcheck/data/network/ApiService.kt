package ru.mokolomyagi.photofactcheck.data.network

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import ru.mokolomyagi.photofactcheck.data.model.LoginRequest
import ru.mokolomyagi.photofactcheck.data.model.LoginResponse
import ru.mokolomyagi.photofactcheck.data.model.WorkOrderSummary

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET("workorders")
    suspend fun getWorkOrders(): Response<List<WorkOrderSummary>>

    @Multipart
    @POST("upload")
    suspend fun uploadImage(@Part image: MultipartBody.Part): Response<Void>
}
