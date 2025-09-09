package ru.mokolomyagi.photofactcheck

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.mokolomyagi.photofactcheck.data.repository.AuthRepository
import ru.mokolomyagi.photofactcheck.data.repository.FakeAuthRepository
import ru.mokolomyagi.photofactcheck.data.repository.IAuthRepository
import ru.mokolomyagi.photofactcheck.ui.login.LoginViewModel
import ru.mokolomyagi.photofactcheck.ui.navigation.AppNavigation
import ru.mokolomyagi.photofactcheck.ui.theme.AppTheme
import ru.mokolomyagi.photofactcheck.ui.workorders.WorkOrdersViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 👉 Подключаем реализацию
        val authRepo: IAuthRepository = FakeAuthRepository()
        // Если нужно будет боевое API:
        // val authRepo: IAuthRepository = AuthRepository(applicationContext)


        // Только у реального репозитория есть метод loadTokenToMemory()
        if (authRepo is AuthRepository) {
            lifecycleScope.launch {
                authRepo.loadTokenToMemory()
            }
        }

        setContent {
            AppTheme {
                val token by authRepo.tokenFlow.collectAsState(initial = null)
                val isAuthorized = !token.isNullOrBlank()

                val loginViewModel = remember { LoginViewModel(authRepo) }
                val workOrdersViewModel = remember { WorkOrdersViewModel() }

                AppNavigation(
                    isAuthorized = isAuthorized,
                    loginViewModel = loginViewModel,
                    workOrdersViewModel = workOrdersViewModel
                )
            }
        }
    }
}
