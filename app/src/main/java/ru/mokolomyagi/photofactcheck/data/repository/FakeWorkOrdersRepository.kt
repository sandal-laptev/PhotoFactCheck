package ru.mokolomyagi.photofactcheck.data.repository

import kotlinx.coroutines.delay
import ru.mokolomyagi.photofactcheck.data.model.WorkOrderDetails
import ru.mokolomyagi.photofactcheck.data.model.WorkOrderSummary

class FakeWorkOrdersRepository : IWorkOrdersRepository {
    private val fakeOrders = listOf(
        WorkOrderSummary("1", "Проверка фото", "В работе", shortDescription = "Нужно сверить изображение"),
        WorkOrderSummary("2", "Отчёт", "Ожидание", shortDescription = "Подготовить ежемесячный отчёт"),
        WorkOrderSummary("3", "Анализ", "Завершено", shortDescription = "Анализировать данные")
    )

    override suspend fun getWorkOrders(): Result<List<WorkOrderSummary>> {
        delay(500)
        return Result.success(fakeOrders)
    }

    override suspend fun getWorkOrderDetails(id: String): Result<WorkOrderDetails> {
        delay(500)
        val order = fakeOrders.find { it.id == id }
        return if (order != null) {
            Result.success(
                WorkOrderDetails(
                    id = order.id,
                    title = order.title,
                    description = order.shortDescription ?: "Подробное описание отсутствует",
                    status = order.status,
                    createdAt = "2025-09-01",
                    assignedTo = "Иван Иванов"
                )
            )
        } else {
            Result.failure(Exception("Наряд не найден"))
        }
    }
}
