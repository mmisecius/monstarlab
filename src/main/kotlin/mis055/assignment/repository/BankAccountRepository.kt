package mis055.assignment.repository

import mis055.assignment.domain.BankAccount
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository
import java.util.UUID

@org.springframework.stereotype.Repository
interface BankAccountRepository : Repository<BankAccount, UUID> {

    @Query("select a from BankAccount a where a.accountNumber = :accountNumber and a.status = mis055.assignment.enums.EntityStatus.ACTIVE")
    fun find(accountNumber: String): BankAccount?

}