package com.example.demo3.service

import com.example.demo3.common.Correlation
import com.example.demo3.common.DateTimeUtils
import com.example.demo3.common.DateTimeUtils.convertToLocalDateTime
import com.example.demo3.exception.InvalidFormatDateTimeException
import com.example.demo3.exception.MissingFieldException
import com.example.demo3.model.HistoryResponse
import com.example.demo3.repository.TransactionHistoryRepository
import org.apache.logging.log4j.LogManager
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class DefaultHistoryService(
    private val transactionHistoryRepository: TransactionHistoryRepository
) : HistoryService {

    override fun getHistory(startDateTime: String, endDateTime: String): ArrayList<HistoryResponse> {
        log.info("[{}] -> [X-Correlation-Id: {}, startDateTime: {}, endDateTime: {}]", "History Service Request", Correlation.get(), startDateTime, endDateTime)
        if (startDateTime.isEmpty()||endDateTime.isEmpty()){
            throw MissingFieldException()
        }
        val startDateTimeFormatted: LocalDateTime
        val endDateTimeFormatted: LocalDateTime
        try {
            startDateTimeFormatted = convertToLocalDateTime(startDateTime, DateTimeUtils.PATTERN_DATE_TIME_SECOND_DASH)
            endDateTimeFormatted = convertToLocalDateTime(endDateTime, DateTimeUtils.PATTERN_DATE_TIME_SECOND_DASH)
        }catch (e:Exception){
            throw InvalidFormatDateTimeException()
        }
        val data = transactionHistoryRepository.findHistoryTransaction(startDateTimeFormatted,endDateTimeFormatted)
        log.info("[{}] -> [X-Correlation-Id: {}, response: {}]", "History Service Response", Correlation.get(), data)
        val response : ArrayList<HistoryResponse> = arrayListOf()
        if (data.isNotEmpty()){
            data.forEach { response.add(HistoryResponse(amount = it.amount, dateTime = it.dateTime.toString()))}
        }
         return response
    }

    companion object {
        private val log = LogManager.getLogger(DefaultHistoryService::class.java)
    }
}