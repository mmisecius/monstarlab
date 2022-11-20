package mis055.assignment.repository

import mis055.assignment.domain.Customer
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository
import java.util.UUID

@org.springframework.stereotype.Repository
interface CustomerRepository : Repository<Customer, UUID> {

    fun findAll(): List<Customer>

    @Query("select c from Customer c where c.id = :id and c.status = mis055.assignment.enums.EntityStatus.ACTIVE")
    fun find(id: UUID): Customer?

}