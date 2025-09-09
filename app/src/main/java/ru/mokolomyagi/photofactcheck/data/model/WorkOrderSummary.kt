package ru.mokolomyagi.photofactcheck.data.model

data class WorkOrderSummary(
    val id: String,
    val title: String,
    val status: String,
    val location: GeoPoint? = null,
    val shortDescription: String? = null
)
