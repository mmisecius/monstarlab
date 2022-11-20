package mis055.assignment.repository

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.testcontainers.junit.jupiter.Testcontainers
import java.util.stream.Stream

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BankAccountRepositoryIT : DbTestSupport() {

    @Autowired
    lateinit var bankAccountRepository: BankAccountRepository


    @ParameterizedTest
    @MethodSource("findTestData")
    fun find(accountNumber: String, found: Boolean) {

        // when
        val result = bankAccountRepository.find(accountNumber)

        // then
        when (found) {
            true -> result != null
            else -> result == null
        }
    }

    companion object {
        val bankAccountNumber1 = "dfg456"
        val bankAccountNumber2 = "cvb456"

        @JvmStatic
        fun findTestData(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(bankAccountNumber1, true),
                Arguments.of(bankAccountNumber2, false)
            )
        }
    }

}