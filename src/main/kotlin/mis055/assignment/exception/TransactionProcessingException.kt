package mis055.assignment.exception

import org.springframework.http.HttpStatus
import org.zalando.problem.AbstractThrowableProblem
import org.zalando.problem.Exceptional
import org.zalando.problem.spring.common.HttpStatusAdapter
import java.io.Serializable
import java.net.URI

class TransactionProcessingException(errorMsg: String, id: Serializable) : AbstractThrowableProblem(
    URI.create("/bankaccount/accountNumber/transactions/send"),
    errorMsg,
    HttpStatusAdapter(HttpStatus.BAD_REQUEST),
    id.toString()
) {
    override fun getCause(): Exceptional? {
        return super.cause
    }
}
