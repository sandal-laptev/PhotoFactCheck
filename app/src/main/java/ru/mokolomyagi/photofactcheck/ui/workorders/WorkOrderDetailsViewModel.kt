package ru.mokolomyagi.photofactcheck.ui.workorders

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.mokolomyagi.photofactcheck.data.repository.FakeWorkOrdersRepository
import ru.mokolomyagi.photofactcheck.data.repository.IWorkOrdersRepository

class WorkOrderDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: IWorkOrdersRepository = FakeWorkOrdersRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(WorkOrderDetailsUiState())
    val uiState: StateFlow<WorkOrderDetailsUiState> = _uiState

    init {
        val orderId = savedStateHandle.get<String>("id") ?: ""
        if (orderId.isNotBlank()) {
            loadDetails(orderId)
        }
    }

    fun loadDetails(orderId: String) {
        viewModelScope.launch {
            _uiState.value = WorkOrderDetailsUiState(isLoading = true)
            val result = repository.getWorkOrderDetails(orderId)
            result.fold(
                onSuccess = { order ->
                    _uiState.value = WorkOrderDetailsUiState(order = order)
                },
                onFailure = { e ->
                    _uiState.value = WorkOrderDetailsUiState(error = e.message ?: "Ошибка загрузки")
                }
            )
        }
    }

    fun addPhotoBefore(photoUri: String) {
        val current = _uiState.value.order ?: return
        _uiState.value = _uiState.value.copy(
            order = current.copy(photosBefore = current.photosBefore + photoUri)
        )
    }

    fun addPhotoAfter(photoUri: String) {
        val current = _uiState.value.order ?: return
        _uiState.value = _uiState.value.copy(
            order = current.copy(photosAfter = current.photosAfter + photoUri)
        )
    }
}
