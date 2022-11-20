package mis055.assignment.handler

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import mis055.assignment.exception.AccountNotFoundException
import mis055.assignment.exception.CustomerNotFoundException
import mis055.assignment.mapper.BankAccountMapper
import mis055.assignment.mapper.CustomerMapper
import mis055.assignment.mapper.TransactionMapper
import mis055.assignment.model.CustomerResponse
import mis055.assignment.model.DetailedBankAccountResponse
import mis055.assignment.model.TransactionResponse
import mis055.assignment.service.CustomerService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("\${rest.api.customer.baseUrl}")
class CustomerHandler(
    private val customerService: CustomerService,
    private val customerMapper: CustomerMapper,
    private val bankAccountMapper: BankAccountMapper,
    private val transactionMapper: TransactionMapper
) {

    @Operation(
        description = "Find a existing customer according to the given id",
        responses = [
            ApiResponse(responseCode = "200", description = "if the customer is found"),
            ApiResponse(responseCode = "404", description = "if the customer is not found")
        ]
    )
    @GetMapping(path = ["/{id}"])
    fun findCustomer(@PathVariable id: UUID): CustomerResponse? {
        logger.info("Going to find the customer entity according to the given id: '$id'")
        return customerService.findCustomer(id)
            ?.let { customerMapper.map(it) }
            ?: throw CustomerNotFoundException(id)
    }

    @GetMapping(path = ["/bankaccount/{accountNumber}"])
    fun findCustomerBankAccount(@PathVariable accountNumber: String): DetailedBankAccountResponse? {
        logger.info("Going to find the customer's bank account entity according to the given accountNumber: '$accountNumber'")
        return customerService.findCustomerBankAccount(accountNumber)
            ?.let { bankAccountMapper.map(it) }
            ?: throw AccountNotFoundException(accountNumber)
    }

    @GetMapping(path = ["/bankaccount/{accountNumber}/transactions"])
    fun findCustomerBankAccountTransactions(
        @PathVariable accountNumber: String,
        pageable: Pageable
    ): Page<TransactionResponse> {
        logger.info("Going to find the customer's bank account's '$accountNumber' transactions")
        return customerService.findCustomerBankAccountTransactions(accountNumber, pageable)
            .let { transactionMapper.map(it) }
    }


    companion object {
        private val logger: Logger = LoggerFactory.getLogger(CustomerHandler::class.java)
    }
}