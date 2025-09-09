package ru.mokolomyagi.photofactcheck.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object WorkOrders : Screen("workorders")
    object WorkOrderDetails : Screen("workorder/{id}") {
        fun createRoute(id: String) = "workorder/$id"
    }
    object Profile : Screen("profile")
}