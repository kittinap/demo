package com.example.demo3.repository

import com.example.demo3.entity.TransactionHistory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface TransactionHistoryRepository : JpaRepository<TransactionHistory,String> {
    @Query("FROM TransactionHistory " +
            "WHERE dateTime BETWEEN :startDateTime AND :endDateTime ")
    fun findHistoryTransaction(startDateTime: LocalDateTime, endDateTime: LocalDateTime):List<TransactionHistory>
}