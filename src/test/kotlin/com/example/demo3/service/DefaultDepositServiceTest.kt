package com.example.demo3.service

import com.example.demo3.entity.TransactionHistory
import com.example.demo3.model.service.DepositServiceRequest
import com.example.demo3.repository.TransactionHistoryRepository
import io.github.benas.randombeans.api.EnhancedRandom
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
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

        val data = EnhancedRandom.random(TransactionHistory::class.java)
        val slotEntity = slot<TransactionHistory>()
        every { transactionHistoryRepository.save(capture(slotEntity)) } answers {data}
        subject.deposit(DepositServiceRequest(LocalDateTime.now(),0.1))
        verify { transactionHistoryRepository.save(capture(slotEntity)) }
    }
}