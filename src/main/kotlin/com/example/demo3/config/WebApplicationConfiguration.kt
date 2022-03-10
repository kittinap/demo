package com.example.demo3.config

import com.example.demo3.prometheus.ControllerHandlerInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebApplicationConfiguration(private val controllerHandlerInterceptor: ControllerHandlerInterceptor) : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(controllerHandlerInterceptor).addPathPatterns("/api/**")
    }
}