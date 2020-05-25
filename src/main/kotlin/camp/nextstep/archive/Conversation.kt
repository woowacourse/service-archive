package camp.nextstep.archive

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
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
        @Lob
        val message: String,

        val userId: String,

        val conversationTime: LocalDateTime,

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0
) : BaseAuditEntity() {
    @JsonManagedReference
    @OneToMany(mappedBy = "conversation", cascade = [CascadeType.MERGE, CascadeType.PERSIST])
    val replies: MutableList<Reply> = mutableListOf()

    fun add(reply: Reply) {
        replies.add(reply)
    }

    fun addAll(replies: List<Reply>) {
        this.replies.addAll(replies)
    }
}

@Entity
class Reply(
        @JsonBackReference
        @ManyToOne
        @JoinColumn(foreignKey = ForeignKey(name = "fk_reply_conversation"))
        val conversation: Conversation,

        @Lob
        val message: String,
        val userId: String,
        val replyTime: LocalDateTime,

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0
) : BaseAuditEntity()
