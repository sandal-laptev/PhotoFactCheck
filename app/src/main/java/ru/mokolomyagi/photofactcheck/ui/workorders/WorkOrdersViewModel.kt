package ru.mokolomyagi.photofactcheck.ui.workorders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.mokolomyagi.photofactcheck.data.repository.FakeWorkOrdersRepository
import ru.mokolomyagi.photofactcheck.data.repository.IWorkOrdersRepository

class WorkOrdersViewModel(
    private val repository: IWorkOrdersRepository = FakeWorkOrdersRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(WorkOrdersUiState())
    val uiState: StateFlow<WorkOrdersUiState> = _uiState

    fun loadWorkOrders() {
        viewModelScope.launch {
            _uiState.value = WorkOrdersUiState(isLoading = true)

            val result = repository.getWorkOrders()
            result.fold(
                onSuccess = { list ->
                    _uiState.value = WorkOrdersUiState(items = list)
                },
                onFailure = { e ->
                    _uiState.value = WorkOrdersUiState(error = e.message ?: "Ошибка загрузки")
                }
            )
        }
    }
}