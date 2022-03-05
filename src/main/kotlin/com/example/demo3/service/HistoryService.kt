package com.example.demo3.service

import com.example.demo3.model.HistoryResponse

interface HistoryService {
    fun getHistory(startDateTime: String, endDateTime: String):ArrayList<HistoryResponse>
}