package mis055.assignment.mapper

import mis055.assignment.domain.Transaction
import mis055.assignment.model.TransactionResponse
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl

@Mapper
abstract class TransactionMapper {

    fun map(transactionPage: Page<Transaction>): Page<TransactionResponse> {
        val transactions = transactionPage.content.map { map(it) }
        return PageImpl(transactions, transactionPage.pageable, transactionPage.totalElements)
    }


    @Mappings(
        Mapping(target = "accountNumber", source = "transaction.account.accountNumber")
    )
    abstract fun map(transaction: Transaction): TransactionResponse

}