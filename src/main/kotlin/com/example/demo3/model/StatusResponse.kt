package com.example.demo3.model

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class StatusResponse(var code: String,
                          var message: String) {

    companion object {
        fun success() = StatusResponse("10000", "Success")
    }
}