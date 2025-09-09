package ru.mokolomyagi.photofactcheck.data.model

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String? = null,
    val expiresIn: Long? = null
)
