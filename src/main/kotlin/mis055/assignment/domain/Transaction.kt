package mis055.assignment.domain

import mis055.assignment.enums.Currency
import mis055.assignment.enums.TransactionStatus
import mis055.assignment.enums.TransactionType
import org.hibernate.annotations.Type
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.persistence.Version

@Table(name = "transaction")
@Entity
class Transaction(
    @Id
    @Type(type = "org.hibernate.type.UUIDCharType")
    val id: UUID,

    @ManyToOne
    @JoinColumn(name = "account_number")
    val account: BankAccount,

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    val transactionType: TransactionType,

    @Column(name = "value_date", nullable = false)
    val valueDate: OffsetDateTime,

    @Column(name = "amount", nullable = false)
    val amount: BigDecimal,

    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false)
    val currency: Currency,

    @Column(name = "variable_symbol", nullable = true)
    val variableSymbol: String?,

    @Column(name = "specific_symbol", nullable = true)
    val specificSymbol: String?,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    val status: TransactionStatus,

    @Version
    @Column(name = "version", nullable = false)
    val version: Int,
)