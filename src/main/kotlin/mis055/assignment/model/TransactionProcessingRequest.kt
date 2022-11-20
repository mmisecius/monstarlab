package mis055.assignment.model

import mis055.assignment.enums.Currency
import java.io.Serializable
import java.math.BigDecimal
import java.util.UUID

data class TransactionProcessingRequest(

    val transactionId: UUID,
    val sourceAccountNumber: String,
    val targetAccountNumber: String,
    val amount: BigDecimal,
    val currency: Currency,
    val variableSymbol: String?,
    val specificSymbol: String?
) : Serializable