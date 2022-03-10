package com.example.demo3.model.service

import java.time.LocalDateTime


data class HistoryServiceRequest (
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime
        )