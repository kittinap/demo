package com.example.demo3.controller

import com.example.demo3.common.DateTimeUtils
import com.example.demo3.config.WebTestConfig
import com.example.demo3.model.controller.DepositRequest
import com.example.demo3.model.controller.HistoryRequest
import com.example.demo3.model.service.DepositServiceRequest
import com.example.demo3.model.service.HistoryInfo
import com.example.demo3.model.service.HistoryServiceRequest
import com.example.demo3.service.DepositService
import com.example.demo3.service.HistoryService
import com.fasterxml.jackson.databind.ObjectMapper
import io.github.benas.randombeans.api.EnhancedRandom
import io.micrometer.core.instrument.MeterRegistry
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(controllers = [TransactionMoneyController::class])
@Import(WebTestConfig::class)
class TransactionMoneyController{

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var meterRegistry: MeterRegistry

    @MockBean
    lateinit var depositService: DepositService

    @MockBean
    lateinit var historyService: HistoryService

    @Test
    fun `Given have deposit with valid data - When call deposit - Should Response Success`(){
        val mapper = ObjectMapper()
        val request = DepositRequest(amount = 1.0,datetime = "2011-10-05T10:48:01+00:00")
        doNothing().`when`(depositService).deposit(DepositServiceRequest(
            DateTimeUtils.convertToLocalDateTime(
            request.datetime,
            DateTimeUtils.PATTERN_DATE_TIME_SECOND_DASH), anyDouble()))
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/deposits")
            .content(mapper.writeValueAsString(request))
            .contentType("application/json"))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful)

    }

    @Test
    fun `Given have deposit with missing date - When call deposit - Should Response missing field`(){
        val mapper = ObjectMapper()
        val request = DepositRequest(amount = 1.0,datetime = "")
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/deposits")
                .content(mapper.writeValueAsString(request))
                .contentType("application/json"))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError)
            .andExpect(MockMvcResultMatchers.jsonPath("$.status.message", CoreMatchers.`is`("[\"must not be blank\"]")))

    }
    @Test
    fun `Given have deposit with invalid date - When call deposit - Should InvalidFormatDateTimeException`(){
        val mapper = ObjectMapper()
        val request = DepositRequest(amount = 1.0,datetime = "dd")
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/deposits")
                .content(mapper.writeValueAsString(request))
                .contentType("application/json"))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError)
            .andExpect(MockMvcResultMatchers.jsonPath("$.status.code", CoreMatchers.`is`("40002")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.status.message", CoreMatchers.`is`("Invalid date-time format ex.[2011-10-05T10:48:01+00:00]")))

    }

    @Test
    fun `Given have history with valid data - When call history - Should Response Success`(){
        val mapper = ObjectMapper()
        val request = HistoryRequest(startDateTime = "2011-10-05T10:48:01+00:00", endDateTime = "2011-10-05T10:48:01+00:00")
        `when`(
            historyService.getHistory(
                HistoryServiceRequest(
                    DateTimeUtils.convertToLocalDateTime(
                        request.startDateTime,
                        DateTimeUtils.PATTERN_DATE_TIME_SECOND_DASH
                    ),
                    DateTimeUtils.convertToLocalDateTime(
                        request.endDateTime,
                        DateTimeUtils.PATTERN_DATE_TIME_SECOND_DASH
                    )
                )
            )
        ).thenAnswer {
            arrayListOf<HistoryInfo>(
                EnhancedRandom.random(
                    HistoryInfo::class.java
                )
            )
        }
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/history")
                .content(mapper.writeValueAsString(request))
                .contentType("application/json"))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
            .andExpect(MockMvcResultMatchers.jsonPath("$.status.code", CoreMatchers.`is`("10000")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.status.message", CoreMatchers.`is`("Success")))

    }

    @Test
    fun `Given have history with missing data - When call history - Should Response Success`(){
        val mapper = ObjectMapper()
        val request = HistoryRequest(startDateTime = "", endDateTime = "2011-10-05T10:48:01+00:00")

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/history")
                .content(mapper.writeValueAsString(request))
                .contentType("application/json"))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError)
            .andExpect(MockMvcResultMatchers.jsonPath("$.status.code", CoreMatchers.`is`("41001")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.status.message", CoreMatchers.`is`("[\"must not be blank\"]")))
    }

    @Test
    fun `Given have history with invalid data - When call history - Should Response Success`(){
        val mapper = ObjectMapper()
        val request = HistoryRequest(startDateTime = "ss", endDateTime = "2011-10-05T10:48:01+00:00")
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/history")
                .content(mapper.writeValueAsString(request))
                .contentType("application/json"))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError)
            .andExpect(MockMvcResultMatchers.jsonPath("$.status.code", CoreMatchers.`is`("40002")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.status.message", CoreMatchers.`is`("Invalid date-time format ex.[2011-10-05T10:48:01+00:00]")))
        }
}