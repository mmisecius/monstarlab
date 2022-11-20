package mis055.assignment.model

import mis055.assignment.enums.Currency
import java.math.BigDecimal
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class OutgoingTransactionRequest(
    @NotEmpty
    val accountNumber: String,

    @NotEmpty
    val amount: BigDecimal,

    @NotNull
    val currency: Currency,
    val variableSymbol: String?,
    val specificSymbol: String?
)