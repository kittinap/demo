package com.example.demo3.model.service

import java.time.LocalDateTime

data class DepositServiceRequest (
    val datetime: LocalDateTime,
    val amount: Double
        )