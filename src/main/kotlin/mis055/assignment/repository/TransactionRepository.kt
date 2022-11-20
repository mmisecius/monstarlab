package mis055.assignment.repository

import mis055.assignment.domain.BankAccount
import mis055.assignment.domain.Transaction
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface TransactionRepository : PagingAndSortingRepository<Transaction, UUID> {

    @Query("select t FROM Transaction t WHERE t.account=:account")
    fun findAll(account: BankAccount, pageable: Pageable): Page<Transaction>

    @Query("select t FROM Transaction t WHERE t.id=:transactionId")
    fun find(transactionId: UUID): Transaction?
}