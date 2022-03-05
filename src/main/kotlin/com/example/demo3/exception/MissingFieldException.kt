package com.example.demo3.exception

import org.springframework.http.HttpStatus

class MissingFieldException : BaseStatusResponseException(HttpStatus.BAD_REQUEST, "40001", "Missing required field")