package com.example.demo3.prometheus



import com.example.demo3.common.Constant.METRIC_EXCEPTION_MESSAGE
import io.micrometer.core.instrument.MeterRegistry
import org.apache.logging.log4j.LogManager
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.*
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class ControllerHandlerInterceptor(private val meterRegistry: MeterRegistry) : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        try {
            request.setAttribute(START_TIME_ATTRIBUTE, System.currentTimeMillis())
        } catch (e: Exception) {
            // Ensure this does not impact to api
            log.warn("unable to set handlerStartTime at preHandle", e.message)
        }

        return true
    }

    override fun afterCompletion(request: HttpServletRequest, response: HttpServletResponse, handler: Any, ex: Exception?) {
        try {
            if (handler !is HandlerMethod) return

            PrometheusDetail(
                    apiName = getApiName(handler),
                    statusCode = response.status,
                    time = request.getAttribute(START_TIME_ATTRIBUTE).let { System.currentTimeMillis() - (it as Long) }
            ).run {
                PrometheusHelper.evaluateTransaction(meterRegistry, this)
            }
        } catch (e: Exception) {
            // ensure this does not impact to api
            log.warn(METRIC_EXCEPTION_MESSAGE, e.message)
        }
    }

    // ========================================== PRIVATE METHOD ==========================================

    private fun getApiName(handler: HandlerMethod): String? {
        return when {
            handler.hasMethodAnnotation(RequestMapping::class.java) -> handler.getMethodAnnotation(RequestMapping::class.java).let {
                it!!.method[0].name.lowercase() + ":" + it.path[0]
            }
            handler.hasMethodAnnotation(GetMapping::class.java) -> "get:" + handler.getMethodAnnotation(GetMapping::class.java)!!.path[0]
            handler.hasMethodAnnotation(PostMapping::class.java) -> "post:" + handler.getMethodAnnotation(PostMapping::class.java)!!.path[0]
            handler.hasMethodAnnotation(PutMapping::class.java) -> "put:" + handler.getMethodAnnotation(PutMapping::class.java)!!.path[0]
            handler.hasMethodAnnotation(DeleteMapping::class.java) -> "delete:" + handler.getMethodAnnotation(DeleteMapping::class.java)!!.path[0]
            else -> null
        }
    }

    // ========================================== STATIC METHOD ==========================================

    companion object {
        private val log = LogManager.getLogger(this::class.java)
        private const val START_TIME_ATTRIBUTE = "handlerStartTime"
    }
}