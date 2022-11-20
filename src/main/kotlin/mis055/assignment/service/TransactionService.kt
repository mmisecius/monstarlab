package mis055.assignment.service

import mis055.assignment.configuration.RabbitMQConfiguration.Companion.transactionQueueName
import mis055.assignment.domain.BankAccount
import mis055.assignment.domain.Transaction
import mis055.assignment.enums.TransactionStatus
import mis055.assignment.enums.TransactionType
import mis055.assignment.exception.AccountNotFoundException
import mis055.assignment.exception.TransactionProcessingException
import mis055.assignment.mapper.TransactionMapper
import mis055.assignment.model.OutgoingTransactionRequest
import mis055.assignment.model.TransactionProcessingRequest
import mis055.assignment.repository.BankAccountRepository
import mis055.assignment.repository.TransactionRepository
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.UUID
import javax.transaction.Transactional


@Service
class TransactionService(
    private val rabbitTemplate: RabbitTemplate,
    private val bankAccountRepository: BankAccountRepository,
    private val transactionRepository: TransactionRepository,
    private val transactionMapper: TransactionMapper,
) {

    fun sendMoney(accountNumber: String, request: OutgoingTransactionRequest) {
        val account: BankAccount = bankAccountRepository.find(accountNumber)
            ?: throw AccountNotFoundException(accountNumber)
        val outgoingTransaction = createNewOutgoingTransaction(account, request)
        transactionRepository.save(outgoingTransaction)

        val processingRequest = transactionMapper.mapRequest(outgoingTransaction)
        rabbitTemplate.convertAndSend(transactionQueueName, processingRequest)
    }


//    {"transactionId":"1b11a940-4af5-4318-ba3d-2ba5eeb473ef","sourceAccountNumber":"abc123","targetAccountNumber":"abc123","amount":50.00,"currency":"EUR","variableSymbol":"test234","specificSymbol":null}


    @Transactional
    @RabbitListener(queues = [transactionQueueName])
    fun processTransactionEvent(event: TransactionProcessingRequest) {

        val sourceBankAccount = bankAccountRepository.find(event.sourceAccountNumber)
            ?: throw TransactionProcessingException("Source bank account does not exit", event.sourceAccountNumber)
        val targetBankAccount = bankAccountRepository.find(event.targetAccountNumber)
            ?: throw TransactionProcessingException("Target bank account does not exit", event.targetAccountNumber)

        val updatedSourceAccount = sourceBankAccount.copy(balance = subtractBalance(sourceBankAccount, event.amount))
        val originalTransaction = checkOriginalTransaction(transactionRepository.find(event.transactionId))

//            ?: throw TransactionProcessingException(
//                "Original transaction on the account does not exist",
//                "{account = ${event.sourceAccountNumber}, transactionId = ${event.transactionId}}"
//            )
        val updatedOriginalTransaction = originalTransaction.copy(
            valueDate = OffsetDateTime.now(),
            status = TransactionStatus.COUNTED
        )
        bankAccountRepository.updateBalance(updatedSourceAccount)
        transactionRepository.save(updatedOriginalTransaction)

        val updatedTargetAccount = targetBankAccount.copy(balance = addBalance(targetBankAccount, event.amount))
        bankAccountRepository.updateBalance(updatedTargetAccount)
        transactionRepository.save(createNewIncomingTransaction(targetBankAccount, event))
    }

    internal fun checkOriginalTransaction(transaction: Transaction?): Transaction {
        return if (transaction == null) {
            throw java.lang.IllegalStateException()
        } else if (transaction.status != TransactionStatus.CREATED) {
            throw java.lang.IllegalStateException()
        } else {
            transaction
        }
    }

    internal fun subtractBalance(account: BankAccount, amount: BigDecimal): BigDecimal {
        val updatedBalance = account.balance.subtract(amount)

        if (updatedBalance.compareTo(BigDecimal.ZERO) == -1) {
            throw TransactionProcessingException(
                "Insufficient balance at account, transaction is suspended",
                account.accountNumber
            )
        }
        return updatedBalance
    }

    internal fun addBalance(account: BankAccount, amount: BigDecimal): BigDecimal {
        return account.balance.add(amount)
    }

    internal fun createNewOutgoingTransaction(
        account: BankAccount,
        request: OutgoingTransactionRequest
    ): Transaction {
        return Transaction(
            id = UUID.randomUUID(),
            transactionType = TransactionType.OUTGOING_PAYMENT,
            account = account,
            targetAccountNumber = request.accountNumber,
            valueDate = null,
            amount = request.amount,
            currency = request.currency,
            variableSymbol = request.variableSymbol,
            specificSymbol = request.specificSymbol,
            status = TransactionStatus.CREATED
        )
    }

    internal fun createNewIncomingTransaction(
        account: BankAccount,
        request: TransactionProcessingRequest
    ): Transaction {
        return Transaction(
            id = UUID.randomUUID(),
            transactionType = TransactionType.INCOMING_PAYMENT,
            account = account,
            sourceAccountNumber = request.sourceAccountNumber,
            valueDate = OffsetDateTime.now(),
            amount = request.amount,
            currency = request.currency,
            variableSymbol = request.variableSymbol,
            specificSymbol = request.specificSymbol,
            status = TransactionStatus.COUNTED
        )
    }
}