package io.github.woowacourse.archive.conversation

import io.github.woowacourse.archive.slack.DateTimeConverter.toLocalDateTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable

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

    @ParameterizedTest
    @MethodSource("createMessageAndResult")
    fun `특정 위치부터 개수만큼 조회`(message: String, size: Int, result: String) {
        conversationRepository.save(Conversation("1", "", toLocalDateTime("1588828683.270200")))
        conversationRepository.save(Conversation("2", "", toLocalDateTime("1588828683.270201")))
        val pivot = conversationRepository.save(Conversation("3", "", toLocalDateTime("1588828683.270202")))
        conversationRepository.save(Conversation("4", "", toLocalDateTime("1588828683.270203")))
        val pageable: Pageable = PageRequest.of(0, 2);

        val conversations = conversationRepository.findByConversationTimeLessThanAndMessageContainingOrderByConversationTimeDesc(pivot.conversationTime, message, pageable)

        assertAll("conversation",
            { assertThat(conversations.content).hasSize(size) },
            { assertThat(conversations.content[0].message).isEqualTo(result) }
        )
    }

    companion object {
        @JvmStatic
        fun createMessageAndResult() = listOf(
            Arguments.of("", 2, "2"),
            Arguments.of("1", 1, "1")
        )
    }
}
