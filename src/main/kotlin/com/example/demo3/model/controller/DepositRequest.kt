package com.example.demo3.model.controller
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Positive

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class DepositRequest (
    @field:NotBlank
    val datetime: String,
    @field:Positive
    val amount: Double
        )