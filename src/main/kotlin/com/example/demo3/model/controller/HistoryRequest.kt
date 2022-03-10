package com.example.demo3.model.controller

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import javax.validation.constraints.NotBlank

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class HistoryRequest (

    @field:NotBlank
    val startDateTime: String,

    @field:NotBlank
    val endDateTime: String
        )