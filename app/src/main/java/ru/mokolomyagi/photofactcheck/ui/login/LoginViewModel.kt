package ru.mokolomyagi.photofactcheck.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.mokolomyagi.photofactcheck.data.repository.IAuthRepository

data class LoginUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)

class LoginViewModel(
    private val authRepository: IAuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _uiState.value = LoginUiState(isLoading = true)

            val result = authRepository.login(username.trim(), password)
            result.fold(
                onSuccess = {
                    _uiState.value = LoginUiState(success = true)
                },
                onFailure = { e ->
                    _uiState.value = LoginUiState(error = e.message ?: "Ошибка авторизации")
                }
            )
        }
    }
}
