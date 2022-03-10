package com.example.demo3.config

import com.example.demo3.common.Correlation
import org.apache.logging.log4j.LogManager
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component


private val logger = LogManager.getLogger{}

@Aspect
@Component
class LogAspect {

    @Suppress("TooGenericExceptionCaught")
    @Around("@annotation(com.example.demo3.annotation.LogExecution)")
    fun logExecutionTime(joinPoint: ProceedingJoinPoint): Any {
        val start = System.currentTimeMillis()
        val signature = joinPoint.signature.toShortString()
        val result = try {
            with(StringBuilder("[$signature] start -> X-Correlation-Id: ${Correlation.get()}," )) {
                    appendParameters(joinPoint.args.filterNotNull())
                logger.info(toString())
            }
            joinPoint.proceed()
        } catch (throwable: Throwable) {
            logger.error("*** Exception during executing $signature,", throwable)
            throw throwable
        }
        val duration = System.currentTimeMillis() - start
        logger.info("[$signature] end -> Finished executing: X-Correlation-Id: ${Correlation.get()}, returned: '$result', duration: $duration ms")
        return result
    }
    private fun StringBuilder.appendParameters(args: List<Any>) {
        append("parameters: [")
        args.forEachIndexed { i, p ->
            append(p.javaClass.simpleName).append("(").append(p.toString()).append(")")
            if (i < args.size - 1) append(", ")
        }
        append("]")
    }

}