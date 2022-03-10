package com.example.demo3.common

object Constant {
    // Prometheus Metrics
    const val METRIC_NAME_DEMO_TRANSACTION_TOTAL = "demo.transaction_total"
    const val METRIC_NAME_DEMO_TRANSACTION_TOTAL_DESC = "Transactions total"
    const val METRIC_NAME_DEMO_PROCESSING_TIME = "demo.processing_time"
    const val METRIC_NAME_DEMO_PROCESSING_TIME_DESC = "Transactions processing time"
    const val METRIC_TAG_API = "api"
    const val METRIC_TAG_STATUS_CODE = "status_code"
    const val METRIC_TAG_IS_EXPECT = "is_expect"
    const val METRIC_EXCEPTION_MESSAGE = "unable to prometheus process message for collecting metrics"
}