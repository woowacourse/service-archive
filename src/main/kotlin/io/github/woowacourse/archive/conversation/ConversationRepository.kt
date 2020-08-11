package io.github.woowacourse.archive.conversation

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface ConversationRepository : JpaRepository<Conversation, Long> {
    fun findAllByOrderByConversationTime(): List<Conversation>

    fun findByConversationTimeLessThanAndMemberDisplayNameContainingOrConversationTimeLessThanAndMessageContainingOrderByConversationTimeDesc(conversationTime: LocalDateTime, message: String, conversationTime2: LocalDateTime, message2: String, pageable: Pageable): Page<Conversation>
}
