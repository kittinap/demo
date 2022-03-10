package com.example.demo3.service

import com.example.demo3.annotation.LogExecution
import com.example.demo3.entity.TransactionHistory
import com.example.demo3.model.service.DepositServiceRequest
import com.example.demo3.repository.TransactionHistoryRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class DefaultDepositService(
    private val transactionHistoryRepository: TransactionHistoryRepository
) : DepositService {

    @Transactional
    @LogExecution
    override fun deposit(data: DepositServiceRequest) {
            transactionHistoryRepository.save(TransactionHistory(dateTime = data.datetime,amount = data.amount))
    }

}