package mis055.assignment.service

import mis055.assignment.domain.BankAccount
import mis055.assignment.domain.Customer
import mis055.assignment.domain.Transaction
import mis055.assignment.exception.AccountNotFoundException
import mis055.assignment.repository.BankAccountRepository
import mis055.assignment.repository.TransactionRepository
import mis055.assignment.repository.CustomerRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class CustomerService(
    private val customerRepository: CustomerRepository,
    private val bankAccountRepository: BankAccountRepository,
    private val transactionRepository: TransactionRepository,
) {
    fun findCustomer(id: UUID): Customer? = customerRepository.find(id)

    fun findCustomerBankAccount(accountNumber: String): BankAccount? =
        bankAccountRepository.find(accountNumber)

    fun findCustomerBankAccountTransactions(accountNumber: String, pageable: Pageable): Page<Transaction> {
        val account = bankAccountRepository.find(accountNumber)
            ?: throw AccountNotFoundException(accountNumber)
        return transactionRepository.findAll(account, pageable)
    }
}