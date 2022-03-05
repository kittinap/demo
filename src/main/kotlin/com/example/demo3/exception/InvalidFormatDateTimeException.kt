package com.example.demo3.exception

import org.springframework.http.HttpStatus

class InvalidFormatDateTimeException : BaseStatusResponseException(HttpStatus.BAD_REQUEST, "40002", "Invalid date-time format ex.[2011-10-05T10:48:01+00:00]")