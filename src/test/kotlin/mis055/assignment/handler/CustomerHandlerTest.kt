package mis055.assignment.handler

import assertk.all
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.index
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.prop
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.mockk
import mis055.assignment.domain.BankAccount
import mis055.assignment.domain.Customer
import mis055.assignment.domain.Transaction
import mis055.assignment.enums.BankAccountType
import mis055.assignment.enums.Country
import mis055.assignment.enums.Currency
import mis055.assignment.enums.TransactionStatus
import mis055.assignment.enums.TransactionType
import mis055.assignment.model.CustomerResponse
import mis055.assignment.model.DetailedBankAccountResponse
import mis055.assignment.model.LightweighBankAccountResponse
import mis055.assignment.model.TransactionResponse
import mis055.assignment.repository.DbTestSupport
import mis055.assignment.service.CustomerService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.UUID
import java.util.stream.Stream


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
internal class CustomerHandlerTest{

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockkBean
    private lateinit var service: CustomerService

    @Value("\${rest.api.customer.baseUrl}")
    private lateinit var customerBaseUrl: String

    @ParameterizedTest
    @MethodSource("findCustomerTestData")
    fun findCustomer(customer: Customer) {

        // given
        val idValue = customer.id
        every { service.findCustomer(idValue) } returns customer

        // when
        val result = mockMvc.perform(get("$customerBaseUrl/{id}", idValue).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk).andReturn().response.asType<CustomerResponse>(objectMapper)

        // then
        assertThat(result).all {
            prop(CustomerResponse::id).isEqualTo(customer.id)
            prop(CustomerResponse::name).isEqualTo(customer.name)
            prop(CustomerResponse::surname).isEqualTo(customer.surname)
            prop(CustomerResponse::citizenship).isEqualTo(customer.citizenship)
            prop(CustomerResponse::associatedAccounts).all {
                hasSize(1)
                index(0).all {
                    val bankAccount = customer.accounts[0]
                    prop(LightweighBankAccountResponse::accountNumber).isEqualTo(bankAccount.accountNumber)
                    prop(LightweighBankAccountResponse::balance).isEqualTo(bankAccount.balance)
                    prop(LightweighBankAccountResponse::currency).isEqualTo(bankAccount.currency)
                }
            }
        }
    }

    @ParameterizedTest
    @MethodSource("findCustomerBankAccountTestData")
    fun findCustomerBankAccount(bankAccount: BankAccount) {

        // given
        val accountNumberValue = bankAccount.accountNumber
        every { service.findCustomerBankAccount(accountNumberValue) } returns bankAccount

        // when
        val result = mockMvc.perform(
            get(
                "$customerBaseUrl/bankaccount/{accountNumber}", accountNumberValue
            ).contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andReturn().response.asType<DetailedBankAccountResponse>(objectMapper)

        // then
        assertThat(result).all {
            prop(DetailedBankAccountResponse::accountNumber).isEqualTo(bankAccount.accountNumber)
            prop(DetailedBankAccountResponse::bankCode).isEqualTo(bankAccount.bankCode)
            prop(DetailedBankAccountResponse::accountType).isEqualTo(bankAccount.accountType)
            prop(DetailedBankAccountResponse::iban).isEqualTo(bankAccount.iban)
            prop(DetailedBankAccountResponse::balance).isEqualTo(bankAccount.balance)
            prop(DetailedBankAccountResponse::currency).isEqualTo(bankAccount.currency)
            val owner = bankAccount.owner
            prop(DetailedBankAccountResponse::owner).isEqualTo("${owner.name} ${owner.surname}")
        }
    }

    @ParameterizedTest
    @MethodSource("findCustomerBankAccountTransactionsTestData")
    fun findCustomerBankAccountTransactions(transactions: List<Transaction>) {

        // given
        val accountNumberValue = "abc123"
        val page = mockk<Page<Transaction>>(relaxed = true) {
            every { content } returns transactions
            every { totalElements } returns transactions.size.toLong()
            every { pageable } returns Pageable.unpaged()
        }
        every { service.findCustomerBankAccountTransactions(accountNumberValue, any()) } returns page

        // when
        val result = mockMvc.perform(
            get(
                "$customerBaseUrl/bankaccount/{accountNumber}/transactions", accountNumberValue
            ).contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andReturn()
            .response.asType<TestPage<TransactionResponse>>(objectMapper)

        // then
        assertThat(result).prop(TestPage<TransactionResponse>::content).all {
            hasSize(2)
            index(0).all {
                val transaction1 = transactions[0]
                prop(TransactionResponse::id).isEqualTo(transaction1.id)
                prop(TransactionResponse::accountNumber).isEqualTo(transaction1.account.accountNumber)
                prop(TransactionResponse::amount).isEqualTo(transaction1.amount)
                prop(TransactionResponse::currency).isEqualTo(transaction1.currency)
                prop(TransactionResponse::valueDate).isNotNull()
                prop(TransactionResponse::variableSymbol).isEqualTo(transaction1.variableSymbol)
                prop(TransactionResponse::specificSymbol).isEqualTo(transaction1.specificSymbol)
                prop(TransactionResponse::transactionType).isEqualTo(transaction1.transactionType)
            }
        }
    }

    companion object {
        inline fun <reified T> MockHttpServletResponse.asType(mapper: ObjectMapper): T {
            return mapper.readValue(this.contentAsString)
        }

        @JvmStatic
        fun findCustomerTestData(): Stream<Arguments> {
            val customer = mockk<Customer> {
                every { id } returns UUID.randomUUID()
                every { name } returns "test"
                every { surname } returns "tester"
                every { citizenship } returns Country.CZ
                every { accounts } returns listOf(mockk {
                    every { accountNumber } returns "abc123"
                    every { balance } returns BigDecimal.TEN
                    every { currency } returns Currency.EUR
                })
            }

            return Stream.of(
                Arguments.of(customer)
            )
        }

        @JvmStatic
        fun findCustomerBankAccountTestData(): Stream<Arguments> {

            val bankAccount = mockk<BankAccount> {
                every { accountNumber } returns "abc123"
                every { bankCode } returns "0200"
                every { accountType } returns BankAccountType.BUSINESS
                every { iban } returns "223344"
                every { balance } returns BigDecimal.TEN
                every { currency } returns Currency.EUR
                every { owner } returns mockk {
                    every { name } returns "Test"
                    every { surname } returns "Tester"
                }
            }
            return Stream.of(
                Arguments.of(bankAccount)
            )
        }

        @JvmStatic
        fun findCustomerBankAccountTransactionsTestData(): Stream<Arguments> {
            val transaction1 = mockk<Transaction> {

                every { id } returns UUID.randomUUID()
                every { account } returns mockk {
                    every { accountNumber } returns "abc123"
                }
                every { transactionType } returns TransactionType.INCOMING_PAYMENT
                every { valueDate } returns OffsetDateTime.now()
                every { amount } returns BigDecimal.valueOf(12.23)
                every { currency } returns Currency.EUR
                every { variableSymbol } returns "456cvb234"
                every { specificSymbol } returns "ytr765"
                every { status } returns TransactionStatus.COUNTED
            }
            val transaction2 = mockk<Transaction> {

                every { id } returns UUID.randomUUID()
                every { account } returns mockk {
                    every { accountNumber } returns "abc123"
                }
                every { transactionType } returns TransactionType.OUTGOING_PAYMENT
                every { valueDate } returns OffsetDateTime.now()
                every { amount } returns BigDecimal.valueOf(3.44)
                every { currency } returns Currency.EUR
                every { variableSymbol } returns "fff432we"
                every { specificSymbol } returns null
                every { status } returns TransactionStatus.COUNTED
            }

            return Stream.of(
                Arguments.of(listOf(transaction1, transaction2))
            )
        }
    }

    class TestPage<T>(val content: List<T>)
}