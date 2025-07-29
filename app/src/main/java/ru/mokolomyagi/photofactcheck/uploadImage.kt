package ru.mokolomyagi.photofactcheck

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

fun uploadImage(file: File) {
    val requestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
    val multipartBody = MultipartBody.Part.createFormData("file", file.name, requestBody)

    val call = RetrofitClient.instance.uploadImage(multipartBody)
    call.enqueue(object : Callback<Void> {
        override fun onResponse(call: Call<Void>, response: Response<Void>) {
            if (response.isSuccessful) {
                // Успешная отправка
            } else {
                // Обработка ошибки
            }
        }

        override fun onFailure(call: Call<Void>, t: Throwable) {
            // Обработка ошибки
        }
    })
}
