package com.example.demo3.controller

import com.example.demo3.annotation.LogExecution
import com.example.demo3.common.Correlation
import com.example.demo3.common.DateTimeUtils
import com.example.demo3.config.SwaggerConfiguration.Companion.DEMO
import com.example.demo3.exception.InvalidFormatDateTimeException
import com.example.demo3.model.BasicResponse
import com.example.demo3.model.controller.DepositRequest
import com.example.demo3.model.controller.HistoryRequest
import com.example.demo3.model.service.DepositServiceRequest
import com.example.demo3.model.service.HistoryInfo
import com.example.demo3.model.service.HistoryServiceRequest
import com.example.demo3.service.DepositService
import com.example.demo3.service.HistoryService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import java.time.format.DateTimeParseException
import javax.validation.Valid

@Validated
@RestController
@Api(tags = [DEMO])
class TestController (
    private val depositService: DepositService,
    private val historyService: HistoryService
    ){

    @LogExecution
    @PostMapping("/api/deposits")
    @ApiOperation(value = "deposit", tags = [DEMO])
    fun deposits(
        @RequestHeader("X-Correlation-Id", required = false) correlationId: String?,
        @Valid @RequestBody request: DepositRequest
    ): ResponseEntity<BasicResponse<String>> {
        Correlation.register(correlationId)
        return try {
            depositService.deposit(
                DepositServiceRequest(
                    datetime = DateTimeUtils.convertToLocalDateTime(
                        request.datetime,
                        DateTimeUtils.PATTERN_DATE_TIME_SECOND_DASH
                    ),
                    amount = request.amount
                )
            )
            ResponseEntity(HttpStatus.CREATED)

        }catch (e: DateTimeParseException){
            throw InvalidFormatDateTimeException()
        }

    }
    @LogExecution
    @PostMapping("/api/history")
    @ApiOperation(value = "history", tags = [DEMO])
   fun history(
        @RequestHeader("X-Correlation-Id", required = false) correlationId: String?,
        @Valid @RequestBody request:  HistoryRequest
    ): BasicResponse<List<HistoryInfo>> {
        Correlation.register(correlationId)
        return try {

            val response = historyService.getHistory(
                HistoryServiceRequest(
                    startDateTime = DateTimeUtils.convertToLocalDateTime(
                        request.startDateTime,
                        DateTimeUtils.PATTERN_DATE_TIME_SECOND_DASH
                    ),
                    endDateTime = DateTimeUtils.convertToLocalDateTime(
                        request.endDateTime,
                        DateTimeUtils.PATTERN_DATE_TIME_SECOND_DASH
                    )
            ))
            BasicResponse(response)

        }catch (e: DateTimeParseException){
            throw InvalidFormatDateTimeException()
        }
    }
}