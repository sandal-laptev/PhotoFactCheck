package ru.mokolomyagi.photofactcheck.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.mokolomyagi.photofactcheck.datastore.ThemeMode
import ru.mokolomyagi.photofactcheck.datastore.UserPreferencesRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val repository = remember { UserPreferencesRepository(context) }

    val coordFormat by repository.coordFormatFlow.collectAsState(initial = "decimal")
    val themeMode by repository.themeFlow.collectAsState(initial = ThemeMode.SYSTEM)

    val formatOptions = mapOf(
        "decimal" to "Десятичные (DD)",
        "dms" to "Градусы/минуты/секунды (DMS)"
    )

    val themeOptions = mapOf(
        ThemeMode.LIGHT to "Светлая",
        ThemeMode.DARK to "Тёмная",
        ThemeMode.SYSTEM to "Системная"
    )

    var formatExpanded by remember { mutableStateOf(false) }
    var themeExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Формат координат
        Text("Формат координат", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        ExposedDropdownMenuBox(
            expanded = formatExpanded,
            onExpandedChange = { formatExpanded = !formatExpanded }
        ) {
            TextField(
                value = formatOptions[coordFormat] ?: coordFormat,
                onValueChange = {},
                readOnly = true,
                label = { Text("Выберите формат") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = formatExpanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = formatExpanded,
                onDismissRequest = { formatExpanded = false }
            ) {
                formatOptions.forEach { (value, label) ->
                    DropdownMenuItem(
                        text = { Text(label) },
                        onClick = {
                            scope.launch {
                                repository.setCoordFormat(value)
                            }
                            formatExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Выбор темы
        Text("Тема оформления", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        ExposedDropdownMenuBox(
            expanded = themeExpanded,
            onExpandedChange = { themeExpanded = !themeExpanded }
        ) {
            TextField(
                value = themeOptions[themeMode] ?: themeMode.name,
                onValueChange = {},
                readOnly = true,
                label = { Text("Выберите тему") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = themeExpanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = themeExpanded,
                onDismissRequest = { themeExpanded = false }
            ) {
                themeOptions.forEach { (value, label) ->
                    DropdownMenuItem(
                        text = { Text(label) },
                        onClick = {
                            scope.launch {
                                repository.setThemeMode(value)
                            }
                            themeExpanded = false
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen()
}
