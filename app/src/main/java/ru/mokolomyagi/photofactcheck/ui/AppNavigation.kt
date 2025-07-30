package ru.mokolomyagi.photofactcheck.ui

import SettingsScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.mokolomyagi.photofactcheck.ui.camera.CameraScreen
import ru.mokolomyagi.photofactcheck.ui.main.MainScreen

sealed class Screen(val route: String) {
    object Main : Screen("main")
    object Camera : Screen("camera")
    object Settings : Screen("settings")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Main.route) {
        composable(Screen.Main.route) {
            MainScreen(
                onCameraClick = { navController.navigate(Screen.Camera.route) },
                onOtherClick = { navController.navigate(Screen.Settings.route) }
            )
        }
        composable(Screen.Camera.route) {
            CameraScreen()
        }
        composable(Screen.Settings.route) {
            SettingsScreen()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppNavigationPreview() {
    AppNavigation()
}