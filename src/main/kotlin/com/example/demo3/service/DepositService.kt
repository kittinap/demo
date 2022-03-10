package com.example.demo3.service

import com.example.demo3.model.service.DepositServiceRequest

interface DepositService {
    fun deposit(data: DepositServiceRequest)
}