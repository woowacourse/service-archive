package camp.nextstep.archive

import org.springframework.data.jpa.repository.JpaRepository

interface ConversationRepository : JpaRepository<Conversation, Long> {
}
