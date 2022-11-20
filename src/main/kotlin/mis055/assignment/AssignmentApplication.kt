package mis055.assignment

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.data.web.config.EnableSpringDataWebSupport

//@EnableWebSecurity
@EnableSpringDataWebSupport
@SpringBootApplication(exclude = [ErrorMvcAutoConfiguration::class])
class AssignmentApplication

fun main(args: Array<String>) {
    runApplication<AssignmentApplication>(*args)
}
