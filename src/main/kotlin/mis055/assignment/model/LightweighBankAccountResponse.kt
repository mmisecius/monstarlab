package mis055.assignment.model

import mis055.assignment.enums.Currency
import java.math.BigDecimal

data class LightweighBankAccountResponse(
    val accountNumber: String,
    val balance: BigDecimal,
    val currency: Currency
)
