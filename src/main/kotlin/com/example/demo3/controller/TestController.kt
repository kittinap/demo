package com.example.demo3.controller

import com.example.demo3.common.Correlation
import com.example.demo3.config.SwaggerConfiguration.Companion.DEMO
import com.example.demo3.model.BasicResponse
import com.example.demo3.model.DepositRequest
import com.example.demo3.model.HistoryRequest
import com.example.demo3.model.HistoryResponse
import com.example.demo3.service.DepositService
import com.example.demo3.service.HistoryService
import com.fasterxml.jackson.databind.ObjectMapper
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.apache.logging.log4j.LogManager
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
@Api(tags = [DEMO])
class TestController (
    private val depositService: DepositService,
    private val historyService: HistoryService
    ){
    private val mapper = ObjectMapper()


    @PostMapping("/api/deposits")
    @ApiOperation(value = "deposit", tags = [DEMO])
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "success"),
        ApiResponse(code = 400, message = "api fail"),
        ApiResponse(code = 500, message = "internal server error")
    ]) fun deposits(
        @RequestHeader("X-Correlation-Id", required = false) correlationId: String?,
        @RequestBody request: DepositRequest
    ): ResponseEntity<BasicResponse<String>> {
        Correlation.register(correlationId)
        log.info("[{}] -> [X-Correlation-Id: {}, request: {}]", "Deposits Request", Correlation.get(), mapper.writeValueAsString(request))
        depositService.deposit(request.datetime,request.amount)
        return ResponseEntity(HttpStatus.CREATED)
    }

    @PostMapping("/api/history")
    @ApiOperation(value = "history", tags = [DEMO])
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "success"),
        ApiResponse(code = 400, message = "api fail"),
        ApiResponse(code = 500, message = "internal server error")
    ]) fun history(
        @RequestHeader("X-Correlation-Id", required = false) correlationId: String?,
        @RequestBody request: HistoryRequest
    ): BasicResponse<List<HistoryResponse>> {
        Correlation.register(correlationId)
        log.info("[{}] -> [X-Correlation-Id: {}, request: {}]", "History Controller Request", Correlation.get(), mapper.writeValueAsString(request))
        val response = historyService.getHistory(request.startDateTime!!,request.endDateTime!!)
        log.info("[{}] -> [X-Correlation-Id: {}, request: {}]", "History Controller Response", Correlation.get(), mapper.writeValueAsString(request))
        return BasicResponse(response)
    }

    companion object {
        private val log = LogManager.getLogger(TestController::class.java)
    }
}