package com.example.demo3.exception

import org.springframework.http.HttpStatus

open class BaseStatusResponseException(val httpStatus: HttpStatus, val code: String, override val message: String, val correlationId: String? = null) : IllegalStateException(message)