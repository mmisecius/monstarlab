package mis055.assignment.mapper

import mis055.assignment.domain.Customer
import mis055.assignment.model.LightweighBankAccountResponse
import mis055.assignment.model.CustomerResponse
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import org.springframework.beans.factory.annotation.Autowired

@Mapper
abstract class CustomerMapper {

    @Autowired
    private lateinit var bankAccountMapper: BankAccountMapper

    @Mappings(
        Mapping(target = "associatedAccounts", expression = "java(convertToList(customer))")
    )
    abstract fun map(customer: Customer): CustomerResponse

    protected fun convertToList(customer: Customer): List<LightweighBankAccountResponse> {
        return customer.accounts.map { bankAccountMapper.mapToLightweight(it) }
    }
}