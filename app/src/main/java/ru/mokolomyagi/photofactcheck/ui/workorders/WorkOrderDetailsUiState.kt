package ru.mokolomyagi.photofactcheck.ui.workorders

import ru.mokolomyagi.photofactcheck.data.model.WorkOrderDetails

data class WorkOrderDetailsUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val order: WorkOrderDetails? = null
)
