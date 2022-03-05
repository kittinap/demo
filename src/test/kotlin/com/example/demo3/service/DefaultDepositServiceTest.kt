package com.example.demo3.service

import com.example.demo3.entity.TransactionHistory
import com.example.demo3.exception.InvalidFormatDateTimeException
import com.example.demo3.exception.MissingFieldException
import com.example.demo3.repository.TransactionHistoryRepository
import io.github.benas.randombeans.api.EnhancedRandom
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDateTime

@ExtendWith(MockKExtension::class)
class DefaultDepositServiceTest{

    private lateinit var subject: DefaultDepositService

    @MockK
    lateinit var transactionHistoryRepository: TransactionHistoryRepository

    @BeforeEach
    internal fun setUp() {
        subject = DefaultDepositService(
            transactionHistoryRepository = transactionHistoryRepository
        )
    }

    @Test
    fun `Given valid input - When call deposit - Should be success and return with data`() {
        val datetime = "2022-03-05T00:00:00+07:00"
        val data = EnhancedRandom.random(TransactionHistory::class.java)
        val slotEntity = slot<TransactionHistory>()
        every { transactionHistoryRepository.save(capture(slotEntity)) } answers {data}
        subject.deposit(datetime,"1")
        verify { transactionHistoryRepository.save(capture(slotEntity)) }
    }

    @Test
    fun `Given amount only - When call deposit - Should be success and return with current datetime`() {
        val data = EnhancedRandom.random(TransactionHistory::class.java)
        val slotEntity = slot<TransactionHistory>()
        every { transactionHistoryRepository.save(capture(slotEntity)) } answers {data}
        subject.deposit(null,"1")
        assertEquals(LocalDateTime.now().dayOfYear, slotEntity.captured.dateTime.dayOfYear)
        verify { transactionHistoryRepository.save(capture(slotEntity)) }
    }

    @Test
    fun `Given datetime only - When call deposit - Should be MissingFieldException`() {
        val datetime = "2022-03-05T00:00:00+07:00"
        assertThrows<MissingFieldException> { subject.deposit(datetime,null) }
    }

    @Test
    fun `Given datetime invalid format - When call deposit - Should be InvalidFormatDateTimeException`() {
        val datetime = "2022-03-05T"
        assertThrows<InvalidFormatDateTimeException> { subject.deposit(datetime,"1") }
    }
}