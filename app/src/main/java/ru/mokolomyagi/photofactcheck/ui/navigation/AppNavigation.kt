package ru.mokolomyagi.photofactcheck.ui.navigation

import ru.mokolomyagi.photofactcheck.ui.workorders.WorkOrderDetailsViewModel
import androidx.compose.runtime.Composable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.mokolomyagi.photofactcheck.data.repository.FakeWorkOrdersRepository
import ru.mokolomyagi.photofactcheck.ui.login.LoginScreen
import ru.mokolomyagi.photofactcheck.ui.login.LoginViewModel
import ru.mokolomyagi.photofactcheck.ui.workorders.*

@Composable
fun AppNavigation(
    isAuthorized: Boolean,
    loginViewModel: LoginViewModel,
    workOrdersViewModel: WorkOrdersViewModel
) {
    val navController = rememberNavController()
    val startDestination = if (isAuthorized) Screen.WorkOrders.route else Screen.Login.route

    NavHost(navController = navController, startDestination = startDestination) {

        // Login Screen
        composable(Screen.Login.route) {
            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.WorkOrders.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // Work Orders List
        composable(Screen.WorkOrders.route) {
            WorkOrdersScreen(
                viewModel = workOrdersViewModel,
                onWorkOrderClick = { orderId ->
                    navController.navigate(Screen.WorkOrderDetails.createRoute(orderId))
                }
            )
        }

        // Work Order Details
        composable(Screen.WorkOrderDetails.route) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("id") ?: ""
            val detailsViewModel: WorkOrderDetailsViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        if (modelClass.isAssignableFrom(WorkOrderDetailsViewModel::class.java)) {
                            @Suppress("UNCHECKED_CAST")
                            return WorkOrderDetailsViewModel(
                                savedStateHandle = SavedStateHandle().apply {
                                    set("id", orderId)
                                },
                                repository = FakeWorkOrdersRepository()
                            ) as T
                        }
                        throw IllegalArgumentException("Unknown ViewModel class")
                    }
                }
            )

            WorkOrderDetailsScreen(
                orderId = orderId,
                viewModel = detailsViewModel
            )
        }
    }
}
