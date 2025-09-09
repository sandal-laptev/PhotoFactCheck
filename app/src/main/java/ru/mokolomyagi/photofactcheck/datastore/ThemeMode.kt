package ru.mokolomyagi.photofactcheck.datastore

enum class ThemeMode(val value: String) {
    LIGHT("light"),
    DARK("dark"),
    SYSTEM("system");

    companion object {
        fun fromValue(value: String) = entries.firstOrNull { it.value == value } ?: SYSTEM
    }
}