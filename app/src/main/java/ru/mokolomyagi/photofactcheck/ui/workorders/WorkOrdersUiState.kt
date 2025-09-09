package ru.mokolomyagi.photofactcheck.ui.workorders

import ru.mokolomyagi.photofactcheck.data.model.WorkOrderSummary

data class WorkOrdersUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val items: List<WorkOrderSummary> = emptyList()
)
