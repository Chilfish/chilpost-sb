package top.chilfish.chilpost.configuration

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.HandlerExceptionResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import top.chilfish.chilpost.error.GlobalExceptionHandler

@Configuration
class WebConfig : WebMvcConfigurer {

    @Autowired
    private lateinit var globalExceptionHandler: GlobalExceptionHandler

    override fun configureHandlerExceptionResolvers(resolvers: MutableList<HandlerExceptionResolver>) {
        resolvers.add(globalExceptionHandler)
    }
}