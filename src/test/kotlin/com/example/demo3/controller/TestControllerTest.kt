package com.example.demo3.controller

import com.example.demo3.config.WebTestConfig
import com.example.demo3.exception.InvalidFormatDateTimeException
import com.example.demo3.exception.MissingFieldException
import com.example.demo3.model.DepositRequest
import com.example.demo3.model.HistoryRequest
import com.example.demo3.model.HistoryResponse
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

@WebMvcTest(controllers = [TestController::class])
@Import(WebTestConfig::class)
class TestControllerTest{

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
        val request : DepositRequest = EnhancedRandom.random(DepositRequest::class.java)
        doNothing().`when`(depositService).deposit(any(), any())
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/deposits")
            .content(mapper.writeValueAsString(request))
            .contentType("application/json"))
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful)

    }

    @Test
    fun `Given have deposit with missing date - When call deposit - Should Response missing field`(){
        val mapper = ObjectMapper()
        val request : DepositRequest = EnhancedRandom.random(DepositRequest::class.java)
        doAnswer { throw MissingFieldException() }.`when`(depositService).deposit(any(), any())
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/deposits")
                .content(mapper.writeValueAsString(request))
                .contentType("application/json"))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError)
            .andExpect(MockMvcResultMatchers.jsonPath("$.status.code", CoreMatchers.`is`("40001")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.status.message", CoreMatchers.`is`("Missing required field")))

    }
    @Test
    fun `Given have deposit with invalid date - When call deposit - Should InvalidFormatDateTimeException`(){
        val mapper = ObjectMapper()
        val request = EnhancedRandom.random(DepositRequest::class.java)
        doAnswer { throw InvalidFormatDateTimeException() }.`when`(depositService).deposit(any(), any())
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
        val request = EnhancedRandom.random(HistoryRequest::class.java)
        `when`(historyService.getHistory(anyString(), anyString())).thenAnswer { arrayListOf<HistoryResponse>(EnhancedRandom.random(HistoryResponse::class.java))  }
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
        val request = EnhancedRandom.random(HistoryRequest::class.java)
        `when`(historyService.getHistory(anyString(), anyString())).thenAnswer { throw MissingFieldException() }
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/history")
                .content(mapper.writeValueAsString(request))
                .contentType("application/json"))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError)
            .andExpect(MockMvcResultMatchers.jsonPath("$.status.code", CoreMatchers.`is`("40001")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.status.message", CoreMatchers.`is`("Missing required field")))
    }

    @Test
    fun `Given have history with invalid data - When call history - Should Response Success`(){
        val mapper = ObjectMapper()
        val request = EnhancedRandom.random(HistoryRequest::class.java)
        `when`(historyService.getHistory(anyString(), anyString())).thenAnswer { throw InvalidFormatDateTimeException() }
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/history")
                .content(mapper.writeValueAsString(request))
                .contentType("application/json"))
            .andExpect(MockMvcResultMatchers.status().is4xxClientError)
            .andExpect(MockMvcResultMatchers.jsonPath("$.status.code", CoreMatchers.`is`("40002")))
            .andExpect(MockMvcResultMatchers.jsonPath("$.status.message", CoreMatchers.`is`("Invalid date-time format ex.[2011-10-05T10:48:01+00:00]")))
        }
}