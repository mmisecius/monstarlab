package mis055.assignment.domain

import mis055.assignment.enums.BankAccountType
import mis055.assignment.enums.Currency
import mis055.assignment.enums.EntityStatus
import java.math.BigDecimal
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.persistence.Version

@Table(name = "bank_account")
@Entity
data class BankAccount(

    @Id
    @Column(name = "account_number")
    val accountNumber: String,

    @Column(name = "bank_code")
    val bankCode: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type")
    val accountType: BankAccountType,

    @Column(name = "iban")
    val iban: String?,

    @Column(name = "balance", nullable = false)
    val balance: BigDecimal = BigDecimal.ZERO,

    @Enumerated(EnumType.STRING)
    val currency: Currency,

    @ManyToOne
    @JoinColumn(name = "owner_id")
    val owner: Customer,

    @Version
    @Column(name = "version", nullable = false)
    val version: Int,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    val status: EntityStatus
)
