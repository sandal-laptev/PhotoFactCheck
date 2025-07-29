package ru.mokolomyagi.photofactcheck

import android.location.Location
import kotlin.math.abs

object GeoFormatConverter {

    enum class Format(val value: String) {
        DECIMAL("decimal"),
        DMS("dms");

        companion object {
            fun fromValue(value: String): Format {
                return Format.entries.firstOrNull { it.value == value } ?: DECIMAL
            }
        }
    }

    fun formatLocation(location: Location, format: Format): String {
        return when (format) {
            Format.DECIMAL -> "Lat: %.5f\nLon: %.5f".format(location.latitude, location.longitude)
            Format.DMS -> "Lat: ${toDMS(location.latitude, true)}\nLon: ${toDMS(location.longitude, false)}"
        }
    }

    private fun toDMS(coord: Double, isLatitude: Boolean): String {
        val absolute = abs(coord)
        val degrees = absolute.toInt()
        val minutesFull = (absolute - degrees) * 60
        val minutes = minutesFull.toInt()
        val seconds = ((minutesFull - minutes) * 60).toInt()

        val direction = when {
            isLatitude && coord >= 0 -> "N"
            isLatitude && coord < 0 -> "S"
            !isLatitude && coord >= 0 -> "E"
            else -> "W"
        }

        return "$degrees°$minutes′$seconds″ $direction"
    }
}