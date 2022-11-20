package mis055.assignment.repository

import assertk.all
import assertk.assertThat
import assertk.assertions.containsAll
import assertk.assertions.extracting
import assertk.assertions.hasSize
import io.mockk.every
import io.mockk.mockk
import mis055.assignment.domain.BankAccount
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Pageable

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TransactionRepositoryIT : DbTestSupport() {

    @Autowired
    lateinit var transactionRepository: TransactionRepository


    @Test
    fun findAll() {

        // given
        val bankAccount = mockk<BankAccount> {
            every { accountNumber } returns "dfg456"
        }
        // when
        val result = transactionRepository.findAll(bankAccount, Pageable.unpaged())

        // then
        assertThat(result).transform { it.content }.all {
            hasSize(2)
            extracting { it.id }.containsAll(transactionId1, transactionId2)
        }
    }

    companion object {
        val transactionId1 = "eb3db211-67f1-4dbb-9d01-ae421e6a4e4a".toUUID()
        val transactionId2 = "6ce00851-b52b-4465-a32c-74286be1be9d".toUUID()
    }

}