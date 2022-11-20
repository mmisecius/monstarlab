package mis055.assignment.repository

import assertk.all
import assertk.assertThat
import assertk.assertions.containsAll
import assertk.assertions.isNotEmpty
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.UUID
import java.util.stream.Stream

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerRepositoryIT : DbTestSupport() {

    @Autowired
    lateinit var customerRepository: CustomerRepository


    @Test
    fun findAll() {

        // when
        val result = customerRepository.findAll()

        // then
        val ids = result.map { c -> c.id }
        assertThat(ids).all {
            isNotEmpty()
            containsAll(customerId1, customerId2)
        }
    }

    @ParameterizedTest
    @MethodSource("findTestData")
    fun find(customerId: UUID, found: Boolean) {

        // when
        val result = customerRepository.find(customerId)

        // then
        when (found) {
            true -> result != null
            else -> result == null
        }
    }

    companion object {
        val customerId1 = "f786717a-7edc-49ff-98b5-1861cba963bd".toUUID()
        val customerId2 = "82caf85d-50ef-4d1a-a061-290b2a6fc962".toUUID()

        @JvmStatic
        fun findTestData(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(customerId1, true),
                Arguments.of(customerId2, false)
            )
        }
    }

}