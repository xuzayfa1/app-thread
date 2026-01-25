package uz.zero.user

import jakarta.persistence.*
import org.hibernate.annotations.ColumnDefault
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.util.*

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)

open class BaseEntity(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,

    @CreatedDate @Temporal(TemporalType.TIMESTAMP) var createdDate: Date? = null,

    @CreatedBy var createdBy: Long? = null,

    @Column(nullable = false) @ColumnDefault(value = "false") var deleted: Boolean = false,

    )



@Entity
@Table(name = "users", schema = "user_management")
class User(
    var username: String,
    var email: String,
    var passwordHash: String,
    var fullName: String? = null
) : BaseEntity()