package ru.mokolomyagi.photofactcheck.data.auth

/** Простой потокобезопасный holder для токена, чтобы Interceptor видел токен синхронно. */
object TokenProvider {
    @Volatile
    var token: String? = null
}
