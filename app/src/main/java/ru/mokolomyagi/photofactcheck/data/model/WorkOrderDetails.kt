package ru.mokolomyagi.photofactcheck.data.model

data class WorkOrderDetails(
    val id: String,
    val title: String,
    val description: String,
    val status: String,
    val createdAt: String,
    val assignedTo: String,
    val location: GeoPoint? = null,
    val materials: List<String> = emptyList(),
    val photosBefore: List<String> = emptyList(),
    val photosAfter: List<String> = emptyList()
)
