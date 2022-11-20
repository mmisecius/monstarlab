package mis055.assignment.service

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import mis055.assignment.domain.BankAccount
import mis055.assignment.domain.Customer
import mis055.assignment.domain.Transaction
import mis055.assignment.exception.AccountNotFoundException
import mis055.assignment.repository.BankAccountRepository
import mis055.assignment.repository.TransactionRepository
import mis055.assignment.repository.CustomerRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

internal class CustomerServiceTest {

    @MockK
    lateinit var customerRepository: CustomerRepository

    @MockK
    lateinit var bankAccountRepository: BankAccountRepository

    @MockK
    lateinit var transactionRepository: TransactionRepository

    @InjectMockKs
    @SpyK
    lateinit var service: CustomerService

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun findCustomer() {

        // given
        val customerId = UUID.randomUUID()
        val customer = mockk<Customer>()
        every { customerRepository.find(customerId) } returns customer

        // when
        val result = service.findCustomer(customerId)

        // then
        assertThat(result).isEqualTo(customer)
        verify(exactly = 1) { customerRepository.find(customerId) }
    }

    @Test
    fun findCustomerBankAccount() {

        // given
        val accountNumber = "abc123"
        val account = mockk<BankAccount>()
        every { bankAccountRepository.find(accountNumber) } returns account

        // when
        val result = service.findCustomerBankAccount(accountNumber)

        // then
        assertThat(result).isEqualTo(account)
        verify(exactly = 1) { bankAccountRepository.find(accountNumber) }
    }

    @Test
    fun findCustomerBankAccountTransactions() {

        // given
        val accountNumber = "abc123"
        val account = mockk<BankAccount>()
        val pageable = mockk<Pageable>()
        val page = mockk<Page<Transaction>>()
        every { bankAccountRepository.find(accountNumber) } returns account
        every { transactionRepository.findAll(account, pageable) } returns page

        // when
        val result = service.findCustomerBankAccountTransactions(accountNumber, pageable)

        // then
        assertThat(result).isEqualTo(page)
        val accountSlot = slot<BankAccount>()
        verify(exactly = 1) {
            bankAccountRepository.find(accountNumber)
            transactionRepository.findAll(capture(accountSlot), pageable)
        }
        assertThat(accountSlot).transform { it.captured }.isEqualTo(account)
    }

    @Test
    fun findCustomerBankAccountTransactions_accountNotFound() {

        // given
        val accountNumber = "abc123"
        val pageable = mockk<Pageable>()
        every { bankAccountRepository.find(accountNumber) } returns null

        // when
        assertThrows<AccountNotFoundException> { service.findCustomerBankAccountTransactions(accountNumber, pageable) }

        // then
        verify(exactly = 1) { bankAccountRepository.find(accountNumber) }
        verify(exactly = 0) { transactionRepository.findAll(any(), pageable) }
    }
}