package com.example.demo3.entity

import java.io.Serializable
import java.time.LocalDateTime
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "transaction_history")
data class TransactionHistory (
    @Id
    var id: String = UUID.randomUUID().toString(),
    val dateTime: LocalDateTime = LocalDateTime.now(),
    val amount: String?=null,
    val updateBy: String?="system"
) : Serializable