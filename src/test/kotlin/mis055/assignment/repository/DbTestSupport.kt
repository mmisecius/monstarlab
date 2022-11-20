package mis055.assignment.repository

import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName
import java.util.UUID

@ActiveProfiles("IT")
abstract class DbTestSupport {

    companion object {
        @JvmStatic
        private val postgresqlContainer = PostgreSQLContainer(DockerImageName.parse("postgres:15.1")).apply {
            start()
        }

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl);
            registry.add("spring.datasource.password", postgresqlContainer::getPassword);
            registry.add("spring.datasource.username", postgresqlContainer::getUsername);
        }

        fun String.toUUID(): UUID = UUID.fromString(this)
    }

}