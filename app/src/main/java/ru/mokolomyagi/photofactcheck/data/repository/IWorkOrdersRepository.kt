package ru.mokolomyagi.photofactcheck.data.repository

import ru.mokolomyagi.photofactcheck.data.model.WorkOrderDetails
import ru.mokolomyagi.photofactcheck.data.model.WorkOrderSummary

interface IWorkOrdersRepository {
    suspend fun getWorkOrders(): Result<List<WorkOrderSummary>>
    suspend fun getWorkOrderDetails(id: String): Result<WorkOrderDetails>
}
