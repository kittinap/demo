package com.example.demo3.service

import com.example.demo3.common.Correlation
import com.example.demo3.common.DateTimeUtils
import com.example.demo3.common.DateTimeUtils.convertToLocalDateTime
import com.example.demo3.entity.TransactionHistory
import com.example.demo3.exception.InvalidFormatDateTimeException
import com.example.demo3.exception.MissingFieldException
import com.example.demo3.repository.TransactionHistoryRepository
import org.apache.logging.log4j.LogManager
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class DefaultDepositService(
    private val transactionHistoryRepository: TransactionHistoryRepository
) : DepositService {
    override fun deposit(datetime: String?, amount: String?) {
        log.info("[{}] -> [X-Correlation-Id: {}, datetime: {}, amount: {}]", "Deposit Request", Correlation.get(), datetime, amount)
        if (amount.isNullOrEmpty()){
            throw MissingFieldException()
        }
        if (datetime.isNullOrEmpty()){
            transactionHistoryRepository.save(TransactionHistory(dateTime = LocalDateTime.now(),amount = amount))
        }else{
            val dateTimeFormatted:LocalDateTime
            try {
                dateTimeFormatted = convertToLocalDateTime(datetime, DateTimeUtils.PATTERN_DATE_TIME_SECOND_DASH)
            }catch (e:Exception){
                throw InvalidFormatDateTimeException()
            }
            transactionHistoryRepository.save(TransactionHistory(dateTime = dateTimeFormatted,amount = amount))
        }
        log.info("[{}] -> [X-Correlation-Id: {}, response: {}]", "Deposit Response", Correlation.get(), HttpStatus.CREATED)
    }

    companion object {
        private val log = LogManager.getLogger(DefaultDepositService::class.java)
    }
}