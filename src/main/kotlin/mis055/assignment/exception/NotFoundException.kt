package mis055.assignment.exception

import org.springframework.http.HttpStatus
import org.zalando.problem.AbstractThrowableProblem
import org.zalando.problem.Exceptional
import org.zalando.problem.spring.common.HttpStatusAdapter
import java.io.Serializable
import java.net.URI
import java.util.UUID

sealed class NotFoundException(type: String, id: Serializable) : AbstractThrowableProblem(
    URI.create(type),
    "'$type' according to the given id the entity has not been found",
    HttpStatusAdapter(HttpStatus.NOT_FOUND),
    id.toString()
) {
    override fun getCause(): Exceptional? {
        return super.cause
    }
}

class CustomerNotFoundException(id: UUID) : NotFoundException("customer", id)
class AccountNotFoundException(accountNumber: String) : NotFoundException("account", accountNumber)