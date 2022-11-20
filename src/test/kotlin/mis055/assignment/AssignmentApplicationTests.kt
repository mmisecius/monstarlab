package mis055.assignment

import assertk.assertThat
import assertk.assertions.isNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class AssignmentApplicationTests {

    @Autowired
    lateinit var applicationContext: ApplicationContext

    @Test
    fun contextLoads() {

        assertThat(applicationContext).isNotNull()
    }
}
