package camp.nextstep.archive

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseAuditEntity protected constructor() {
    @CreatedDate
    @Column(nullable = false)
    var createdDate: LocalDateTime? = null
        protected set

    @LastModifiedDate
    @Column(nullable = false)
    var updatedDate: LocalDateTime? = null
        protected set
}

@Entity
class Conversation(
        val message: String,

        val userId: String,

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0
) : BaseAuditEntity() {
    override fun toString(): String {
        return "Conversation(message='$message', userId='$userId', id=$id)"
    }
}
