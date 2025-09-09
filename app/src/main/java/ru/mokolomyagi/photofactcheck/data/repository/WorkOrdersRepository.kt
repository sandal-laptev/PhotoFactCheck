package ru.mokolomyagi.photofactcheck.data.repository

import ru.mokolomyagi.photofactcheck.data.model.RetrofitClient
import ru.mokolomyagi.photofactcheck.data.model.WorkOrderSummary

class WorkOrdersRepository {
    suspend fun getWorkOrders(): Result<List<WorkOrderSummary>> {
        return try {
            val response = RetrofitClient.instance.getWorkOrders()
            if (response.isSuccessful) {
                Result.success(response.body().orEmpty())
            } else {
                Result.failure(Exception("Ошибка загрузки: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
