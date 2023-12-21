package top.chilfish.chilpost

import org.jetbrains.exposed.spring.autoconfigure.ExposedAutoConfiguration
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.web.servlet.ServletComponentScan
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import top.chilfish.chilpost.utils.initFileDir
import top.chilfish.chilpost.utils.logger

@SpringBootApplication
@ServletComponentScan
@ImportAutoConfiguration(
    value = [ExposedAutoConfiguration::class],
    exclude = [DataSourceTransactionManagerAutoConfiguration::class]
)
class ChilpostApplication : SpringBootServletInitializer() {
    override fun configure(application: SpringApplicationBuilder): SpringApplicationBuilder {
        return application.sources(ChilpostApplication::class.java)
    }
}

fun configureApplication(builder: SpringApplicationBuilder): SpringApplicationBuilder {
    return builder.sources(ChilpostApplication::class.java)
}

fun main(args: Array<String>) {
    configureApplication(SpringApplicationBuilder()).run(*args)

    logger.info("ChilpostApplication started")
    initFileDir()
}

