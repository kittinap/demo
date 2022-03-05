package com.example.demo3.model

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class BasicResponse<T>(val status: StatusResponse, val data:T? = null) {

    constructor(data: T?) : this(data = data, status = StatusResponse.success())

    companion object{
        fun success() = BasicResponse<Any>(StatusResponse.success())
        fun <T> success(data: T?= null) = BasicResponse<T>(StatusResponse.success(), data)
        fun error(code: String, message: String) = BasicResponse<Any>(StatusResponse(code,message))
    }
}