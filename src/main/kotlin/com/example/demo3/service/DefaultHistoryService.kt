package com.example.demo3.service

import com.example.demo3.annotation.LogExecution
import com.example.demo3.model.service.HistoryInfo
import com.example.demo3.model.service.HistoryServiceRequest
import com.example.demo3.repository.TransactionHistoryRepository
import org.springframework.stereotype.Service

@Service
class DefaultHistoryService(
    private val transactionHistoryRepository: TransactionHistoryRepository
) : HistoryService {

    @LogExecution
    override fun getHistory(data: HistoryServiceRequest): List<HistoryInfo> = transactionHistoryRepository
        .findHistoryTransaction(data.startDateTime, data.endDateTime)
        .map { HistoryInfo(dateTime = it.dateTime.toString(), amount = it.amount) }


}