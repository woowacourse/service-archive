package io.github.woowacourse.archive.conversation

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface ConversationRepository : JpaRepository<Conversation, Long> {
    fun findAllByOrderByConversationTime(): List<Conversation>

    fun findByConversationTimeLessThanAndMessageContainingOrderByConversationTimeDesc(conversationTime: LocalDateTime, message: String, pageable: Pageable): Page<Conversation>
}
