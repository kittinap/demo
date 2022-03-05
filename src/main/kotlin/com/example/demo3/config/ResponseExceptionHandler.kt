package com.example.demo3.config


import com.example.demo3.common.Correlation
import com.example.demo3.exception.BaseStatusResponseException
import com.example.demo3.model.BasicResponse
import org.apache.logging.log4j.LogManager
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler


@ControllerAdvice
class ResponseExceptionHandler : ResponseEntityExceptionHandler() {

    companion object {
       private val log = LogManager.getLogger(ResponseExceptionHandler::class.java)
    }

    @ExceptionHandler(value = [BaseStatusResponseException::class])
    protected fun handle(ex: BaseStatusResponseException, request: WebRequest):ResponseEntity<Any> {
        log.error("[{}] -> [X-Correlation-Id: {}, payload: {}]",
                this.javaClass.simpleName + " : BaseStatusResponseException", Correlation.get(),
                "Exception: ".plus(ex).plus(", message: ").plus(ex.localizedMessage))

        return handleExceptionInternal(
            ex,
            BasicResponse.error(ex.code, ex.message),
            HttpHeaders(),
            ex.httpStatus,
            request
        )
    }

    @ExceptionHandler(value = [Exception::class])
    protected fun handle(ex: Exception, request: WebRequest): ResponseEntity<Any> {
        log.error("[{}] -> [X-Correlation-Id: {},  payload: {}]",
                this.javaClass.simpleName + " : Exception", Correlation.get(),
                "Exception: ".plus(ex).plus(", message: ").plus(ex.localizedMessage))
        ex.printStackTrace()
        return handleExceptionInternal(
                ex,
                BasicResponse.error("51017", "Internal Service Error"),
                HttpHeaders(),
                INTERNAL_SERVER_ERROR,
                request
        )
    }

}