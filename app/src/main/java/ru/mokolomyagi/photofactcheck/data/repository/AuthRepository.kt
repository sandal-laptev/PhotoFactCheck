package ru.mokolomyagi.photofactcheck.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import ru.mokolomyagi.photofactcheck.data.auth.TokenProvider
import ru.mokolomyagi.photofactcheck.data.model.LoginRequest
import ru.mokolomyagi.photofactcheck.data.model.RetrofitClient
import ru.mokolomyagi.photofactcheck.datastore.userPreferencesDataStore

class AuthRepository(private val context: Context) : IAuthRepository {

    companion object {
        private val AUTH_TOKEN_KEY = stringPreferencesKey("auth_token")
    }

    override val tokenFlow: Flow<String?> =
        context.userPreferencesDataStore.data.map { prefs -> prefs[AUTH_TOKEN_KEY] }

    // загрузка токена при старте приложения в TokenProvider
    suspend fun loadTokenToMemory() {
        val token = tokenFlow.first()
        TokenProvider.token = token
    }

    override suspend fun login(username: String, password: String): Result<Unit> {
        return try {
            val resp = RetrofitClient.instance.login(LoginRequest(username, password))
            if (resp.isSuccessful) {
                val body = resp.body() ?: return Result.failure(Exception("Empty body"))
                saveToken(body.accessToken)
                Result.success(Unit)
            } else {
                val code = resp.code()
                val errorBody = resp.errorBody()?.string()
                Result.failure(Exception("Auth failed: $code ${errorBody ?: ""}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun saveToken(token: String) {
        context.userPreferencesDataStore.edit { prefs ->
            prefs[AUTH_TOKEN_KEY] = token
        }
        TokenProvider.token = token
    }

    override suspend fun logout() {
        context.userPreferencesDataStore.edit { prefs ->
            prefs.remove(AUTH_TOKEN_KEY)
        }
        TokenProvider.token = null
    }
}
