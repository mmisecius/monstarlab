package mis055.assignment.domain

import mis055.assignment.enums.Country
import mis055.assignment.enums.EntityStatus
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import org.hibernate.annotations.Type
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table
import javax.persistence.Version

@Table(name = "customer")
@Entity
data class Customer(
    @Id
    @Type(type = "org.hibernate.type.UUIDCharType")
    val id: UUID = UUID.randomUUID(),

    @Column(name = "name", nullable = false)
    val name: String,

    @Column(name = "surname", nullable = false)
    val surname: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "citizenship", nullable = false)
    val citizenship: Country,

    @Version
    @Column(name = "version", nullable = false)
    val version: Int,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    val status: EntityStatus,

    @Fetch(FetchMode.JOIN)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
    val accounts: List<BankAccount>
)