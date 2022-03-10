package com.example.demo3.service

import com.example.demo3.entity.TransactionHistory
import com.example.demo3.model.service.HistoryServiceRequest
import com.example.demo3.repository.TransactionHistoryRepository
import io.github.benas.randombeans.api.EnhancedRandom
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDateTime

@ExtendWith(MockKExtension::class)
class DefaultHistoryServiceTest{

    private lateinit var subject: DefaultHistoryService

    @MockK
    lateinit var transactionHistoryRepository: TransactionHistoryRepository

    @BeforeEach
    internal fun setUp() {
        subject = DefaultHistoryService(
            transactionHistoryRepository = transactionHistoryRepository
        )
    }

    @Test
    fun `Given valid data - When call getHistory - Should be success and return result`() {
        val dateTime = LocalDateTime.now()
        val data = listOf<TransactionHistory>(EnhancedRandom.random(TransactionHistory::class.java))
        every { transactionHistoryRepository.findHistoryTransaction(any(),any()) } answers {data}
        val actual = subject.getHistory(HistoryServiceRequest(dateTime, dateTime))
        assertEquals(actual.size,1)
        verify { transactionHistoryRepository.findHistoryTransaction(any(),any()) }
    }
}