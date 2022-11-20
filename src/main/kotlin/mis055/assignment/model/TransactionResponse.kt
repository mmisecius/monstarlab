package mis055.assignment.model

import mis055.assignment.enums.Currency
import mis055.assignment.enums.TransactionType
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.UUID

data class TransactionResponse(
    val id: UUID,
    val accountNumber: String,
    val sourceAccountNumber: String? = null,
    val targetAccountNumber: String? = null,
    val transactionType: TransactionType,
    val valueDate: OffsetDateTime,
    val amount: BigDecimal,
    val currency: Currency,
    val variableSymbol: String?,
    val specificSymbol: String?
)