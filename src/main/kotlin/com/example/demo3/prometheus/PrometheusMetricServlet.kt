package com.example.demo3.prometheus

import org.apache.catalina.Host
import org.apache.catalina.core.StandardContext
import org.apache.catalina.startup.Tomcat
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory
import org.springframework.boot.web.servlet.ServletContextInitializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.IOException
import java.util.*
import javax.servlet.*

@Configuration
class PrometheusMetricServlet {

    @Bean
    fun servletWebServerFactory(): TomcatServletWebServerFactory? {
        return object : TomcatServletWebServerFactory() {
            override fun prepareContext(host: Host, initializers: Array<ServletContextInitializer?>?) {
                super.prepareContext(host, initializers)
                val child = StandardContext()
                child.addLifecycleListener(Tomcat.FixContextListener())
                child.path = "/metrics"
                val initializer = getServletContextInitializer(contextPath)
                child.addServletContainerInitializer(initializer, Collections.emptySet())
                child.crossContext = true
                host.addChild(child)
            }
        }
    }

    private fun getServletContextInitializer(contextPath: String): ServletContainerInitializer {
        return ServletContainerInitializer { _: Set<Class<*>?>?, context: ServletContext ->
            val servlet: Servlet = object : GenericServlet() {
                @Throws(ServletException::class, IOException::class)
                override fun service(req: ServletRequest, res: ServletResponse) {
                    val contexts = req.servletContext.getContext(contextPath)
                    contexts.getRequestDispatcher("/metrics").forward(req, res)
                }
            }
            context.addServlet("metrics", servlet).addMapping("/*")
        }
    }
}
