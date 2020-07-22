package io.github.woowacourse.archive.conversation

import io.github.woowacourse.archive.slack.DateTimeConverter.toLocalDateTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest

class ConversationRepositoryTest @Autowired constructor(
        val conversationRepository: ConversationRepository
) : IntegrationTest() {

    @Test
    fun `Reply 객체가 저장하는지 확인`() {
        conversation.add(assemble(messages[0]))
        conversation.add(assemble(messages[1]))

        val expected = conversationRepository.save(conversation)
        val actual = conversationRepository
                .findById(expected.id)
                .orElseThrow { throw NoSuchElementException("객체를 찾을 수 없습니다.") }

        assertThat(actual).isEqualTo(expected)
        assertThat(actual.replies.size).isEqualTo(messages.size)
    }

    @Test
    fun `대화시간(nanosecond를 포함)을 그대로 저장하는지 확인`() {
        val persistConversation = conversationRepository.save(conversation)

        assertThat(persistConversation.conversationTime).isEqualTo(conversation.conversationTime)
    }

    @Test
    fun `특정 위치부터 개수만큼 조회`() {
        conversationRepository.save(Conversation("1", "", toLocalDateTime("1588828683.270200")))
        val pivot = conversationRepository.save(Conversation("2", "", toLocalDateTime("1588828683.270200")))
        conversationRepository.save(Conversation("3", "", toLocalDateTime("1588828683.270200")))

        val conversations = conversationRepository.findByIdLessThanOrderByIdDesc(pivot.id, PageRequest.of(0, 2))

        assertThat(conversations.content[0].message).isEqualTo("1")
    }
}
