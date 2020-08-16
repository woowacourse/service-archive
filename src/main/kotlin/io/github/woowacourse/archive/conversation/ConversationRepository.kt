package io.github.woowacourse.archive.conversation

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

interface ConversationRepository : JpaRepository<Conversation, Long> {
    fun findAllByOrderByConversationTime(): List<Conversation>

    @Query("SELECT c FROM Conversation c " +
            "WHERE c.member.displayName LIKE %:message% AND c.conversationTime < :conversationTime " +
            "OR c.message LIKE %:message% AND c.conversationTime < :conversationTime " +
            "ORDER BY c.conversationTime DESC ")
    fun findByMemberNameOrMessageBeforeTimeOrderDesc(conversationTime: LocalDateTime,
                                                     message: String,
                                                     pageable: Pageable): Page<Conversation>
}
