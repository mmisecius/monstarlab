package mis055.assignment.mapper

import mis055.assignment.domain.Transaction
import mis055.assignment.model.TransactionProcessingRequest
import mis055.assignment.model.TransactionResponse
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl

@Mapper
abstract class TransactionMapper {

    fun map(transactionPage: Page<Transaction>): Page<TransactionResponse> {
        val transactions = transactionPage.content.map { mapResponse(it) }
        return PageImpl(transactions, transactionPage.pageable, transactionPage.totalElements)
    }


    @Mappings(
        Mapping(target = "accountNumber", source = "account.accountNumber")
    )
    abstract fun mapResponse(transaction: Transaction): TransactionResponse

    @Mappings(
        Mapping(target = "transactionId", source = "transaction.id"),
        Mapping(target = "sourceAccountNumber", source = "transaction.account.accountNumber"),
        Mapping(target = "targetAccountNumber", source = "transaction.targetAccountNumber"),
        Mapping(target = "amount", source = "transaction.amount"),
        Mapping(target = "currency", source = "transaction.currency"),
        Mapping(target = "variableSymbol", source = "transaction.variableSymbol"),
        Mapping(target = "specificSymbol", source = "transaction.specificSymbol"),
    )
    abstract fun mapRequest(transaction: Transaction): TransactionProcessingRequest

}