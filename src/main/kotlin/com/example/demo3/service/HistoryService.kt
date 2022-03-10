package com.example.demo3.service

import com.example.demo3.model.service.HistoryInfo
import com.example.demo3.model.service.HistoryServiceRequest

interface HistoryService {
    fun getHistory(data: HistoryServiceRequest):List<HistoryInfo>
}