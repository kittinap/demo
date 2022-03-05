package com.example.demo3.common

import org.apache.logging.log4j.ThreadContext
import java.util.*

object Correlation {

    private const val CORRELATION_KEY = "trans_id"

    fun register(correlationId: String? = null): String {
        val generatedCorrelationId = correlationId ?: "demo-${UUID.randomUUID().toString().replace("-", "")}"
        ThreadContext.put(CORRELATION_KEY, generatedCorrelationId)
        return generatedCorrelationId
    }

    fun get() = ThreadContext.get(CORRELATION_KEY) ?: register()

}