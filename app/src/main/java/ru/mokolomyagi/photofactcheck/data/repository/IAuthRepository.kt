package ru.mokolomyagi.photofactcheck.data.repository

import kotlinx.coroutines.flow.Flow

interface IAuthRepository {
    val tokenFlow: Flow<String?>
    suspend fun login(username: String, password: String): Result<Unit>
    suspend fun logout()
}
