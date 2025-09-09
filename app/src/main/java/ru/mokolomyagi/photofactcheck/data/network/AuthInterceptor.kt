package ru.mokolomyagi.photofactcheck.data.network

import okhttp3.Interceptor
import okhttp3.Response
import ru.mokolomyagi.photofactcheck.data.auth.TokenProvider

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val token = TokenProvider.token
        val request = if (!token.isNullOrBlank()) {
            original.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else original
        return chain.proceed(request)
    }
}
