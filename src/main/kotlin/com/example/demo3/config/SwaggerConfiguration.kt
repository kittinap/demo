package com.example.demo3.config

import org.springframework.boot.info.BuildProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.PathSelectors.ant
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Tag
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2


@Configuration
@EnableSwagger2
class SwaggerConfiguration {

    companion object {
        const val DEMO = "demo"
    }

    @Bean
    fun api(buildProperties: BuildProperties): Docket {
        return Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .select()
                .paths(ant("/api/**"))
                .build()
                .tags(
                        TitleTag(DEMO),
                )
                .apiInfo(ApiInfo(
                        "Virtual Demo APIs",
                        "Manage Virtual Demo Feature",
                        buildProperties.version,
                        "",
                        null,
                        "",
                        "",
                        emptyList()
                ))
    }
}

class TitleTag(name: String) : Tag(name, "")