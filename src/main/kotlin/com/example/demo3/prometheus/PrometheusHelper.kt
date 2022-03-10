package com.example.demo3.prometheus


import com.example.demo3.common.Constant.METRIC_NAME_DEMO_PROCESSING_TIME
import com.example.demo3.common.Constant.METRIC_NAME_DEMO_PROCESSING_TIME_DESC
import com.example.demo3.common.Constant.METRIC_NAME_DEMO_TRANSACTION_TOTAL
import com.example.demo3.common.Constant.METRIC_NAME_DEMO_TRANSACTION_TOTAL_DESC
import com.example.demo3.common.Constant.METRIC_TAG_API
import com.example.demo3.common.Constant.METRIC_TAG_IS_EXPECT
import com.example.demo3.common.Constant.METRIC_TAG_STATUS_CODE
import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.Gauge
import io.micrometer.core.instrument.MeterRegistry
import java.util.concurrent.atomic.AtomicLong

object PrometheusHelper {

    fun evaluateTransaction(meterRegistry: MeterRegistry, prometheusDetail: PrometheusDetail) {
        countTransaction(meterRegistry, prometheusDetail)
        gaugeTransactionProcessingTime(meterRegistry, prometheusDetail)
    }

    // ========================================== PRIVATE METHOD ==========================================

    private fun countTransaction(meterRegistry: MeterRegistry, prometheusDetail: PrometheusDetail) {
        val (apiName, statusCode) = prometheusDetail

        return Counter.builder(METRIC_NAME_DEMO_TRANSACTION_TOTAL)
                .description(METRIC_NAME_DEMO_TRANSACTION_TOTAL_DESC)
                .tags(METRIC_TAG_API, apiName,
                        METRIC_TAG_IS_EXPECT, prometheusDetail.getIsExpect(),
                        METRIC_TAG_STATUS_CODE, statusCode.toString())
                .register(meterRegistry)
                .increment()
    }

    private val apiNameWithProcessTimeList = HashMap<String, AtomicLong> ()
    private fun gaugeTransactionProcessingTime(meterRegistry: MeterRegistry, prometheusDetail: PrometheusDetail) {
        val (apiName, statusCode, time) = prometheusDetail
        val isExpect = prometheusDetail.getIsExpect()
        val key = "${apiName!!}_${isExpect}_$statusCode"

        apiNameWithProcessTimeList[key] = AtomicLong(time!!)

        Gauge.builder(METRIC_NAME_DEMO_PROCESSING_TIME, apiNameWithProcessTimeList, { it[key]?.toDouble()!! } )
                .description(METRIC_NAME_DEMO_PROCESSING_TIME_DESC)
                .tags(METRIC_TAG_API, apiName,
                        METRIC_TAG_IS_EXPECT, isExpect,
                        METRIC_TAG_STATUS_CODE, statusCode.toString())
                .register(meterRegistry)
    }
}

// ========================================== DATA CLASS ==========================================

data class PrometheusDetail(val apiName: String?, val statusCode: Int, val time: Long? = null) {
    fun getIsExpect(): String {
       return when(statusCode) {
           in 200..299 -> "0"
           in 300..499 -> "2"
           else -> "1"
       }
    }
}
