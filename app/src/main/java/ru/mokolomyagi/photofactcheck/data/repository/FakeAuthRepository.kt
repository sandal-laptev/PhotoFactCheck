package ru.mokolomyagi.photofactcheck.data.repository

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeAuthRepository : IAuthRepository {

    private val _tokenFlow = MutableStateFlow<String?>(null)
    override val tokenFlow: Flow<String?> = _tokenFlow

    private var token: String? = null

    override suspend fun login(username: String, password: String): Result<Unit> {
        delay(500) // имитация запроса
        return if (username == "user" && password == "1234") {
            token = "fake_token_123"
            _tokenFlow.value = token
            Result.success(Unit)
        } else {
            Result.failure(Exception("Неверный логин или пароль"))
        }
    }

    override suspend fun logout() {
        token = null
        _tokenFlow.value = null
    }
}
