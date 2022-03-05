package com.example.demo3.service

import com.example.demo3.entity.TransactionHistory
import com.example.demo3.exception.InvalidFormatDateTimeException
import com.example.demo3.exception.MissingFieldException
import com.example.demo3.repository.TransactionHistoryRepository
import io.github.benas.randombeans.api.EnhancedRandom
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith

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
    fun `Given startDate or endDate is null or empty - When call getHistory - Should be MissingFieldException`() {
        assertThrows<MissingFieldException> { subject.getHistory("","") }
        assertThrows<MissingFieldException> { subject.getHistory("2","") }
        assertThrows<MissingFieldException> { subject.getHistory("","1") }
    }

    @Test
    fun `Given startDate or endDate is invaild format - When call getHistory - Should be InvalidFormatDateTimeException`() {
        val invalidDatetime = "2022-03-05T"
        val validDatetime = "2022-03-05T00:00:00+07:00"
        assertThrows<InvalidFormatDateTimeException> { subject.getHistory(invalidDatetime,invalidDatetime) }
        assertThrows<InvalidFormatDateTimeException> { subject.getHistory(invalidDatetime,validDatetime) }
        assertThrows<InvalidFormatDateTimeException> { subject.getHistory(validDatetime,invalidDatetime) }
    }

    @Test
    fun `Given valid data - When call getHistory - Should be success and return result`() {

        val validDatetime = "2022-03-05T00:00:00+07:00"
        val data = listOf<TransactionHistory>(EnhancedRandom.random(TransactionHistory::class.java))
        every { transactionHistoryRepository.findHistoryTransaction(any(),any()) } answers {data}
        val actual = subject.getHistory(validDatetime,validDatetime)
        assertEquals(actual.size,1)
        verify { transactionHistoryRepository.findHistoryTransaction(any(),any()) }
    }
}