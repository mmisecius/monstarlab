package mis055.assignment.mapper

import mis055.assignment.domain.BankAccount
import mis055.assignment.model.DetailedBankAccountResponse
import mis055.assignment.model.LightweighBankAccountResponse
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper
abstract class BankAccountMapper {

    @Mappings(
        Mapping(target = "owner", expression = "java(getOwner(account))")
    )
    abstract fun map(account: BankAccount): DetailedBankAccountResponse
    abstract fun mapToLightweight(account: BankAccount): LightweighBankAccountResponse

    protected fun getOwner(account: BankAccount): String {
        return with(account.owner) { "$name $surname" }
    }
}