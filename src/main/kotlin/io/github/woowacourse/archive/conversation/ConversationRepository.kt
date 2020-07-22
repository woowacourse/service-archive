package io.github.woowacourse.archive.conversation

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface ConversationRepository : JpaRepository<Conversation, Long> {
    fun findAllByOrderByConversationTime(): List<Conversation>

    fun findByIdLessThanOrderByIdDesc(id: Long, pageable: Pageable): Page<Conversation>
}
