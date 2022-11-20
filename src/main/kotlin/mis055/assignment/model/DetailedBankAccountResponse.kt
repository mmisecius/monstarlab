package mis055.assignment.model

import mis055.assignment.enums.BankAccountType
import mis055.assignment.enums.Currency
import java.math.BigDecimal

data class DetailedBankAccountResponse(
    val accountNumber: String,
    val bankCode: String,
    val iban: String?,
    val accountType: BankAccountType,
    val balance: BigDecimal,
    val currency: Currency,
    val owner: String
)
