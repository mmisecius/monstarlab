package mis055.assignment.model

import mis055.assignment.enums.Country
import java.util.UUID

data class CustomerResponse(
    val id: UUID,
    val name: String,
    val surname: String,
    val citizenship: Country,

    val associatedAccounts: List<LightweighBankAccountResponse>
)