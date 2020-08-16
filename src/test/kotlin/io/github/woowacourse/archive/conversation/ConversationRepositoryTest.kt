package io.github.woowacourse.archive.conversation

import io.github.woowacourse.archive.member.Member
import io.github.woowacourse.archive.member.MemberRepository
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
import java.time.LocalDateTime

class ConversationRepositoryTest @Autowired constructor(
        val conversationRepository: ConversationRepository,
        val memberRepository: MemberRepository
) : IntegrationTest() {

    @Test
    fun `Reply 객체가 저장하는지 확인`() {
        conversation.add(assemble(messages[0]))
        conversation.add(assemble(messages[1]))

        memberRepository.save(Member("USU9TR4HM", "닉네임", "https://avatars.slack-edge.com/2020-01-17/900291967601_063326588d6eff8f814a_192.png"))

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
        val persistMember = memberRepository.save(Member("USU9TR4HM", "닉네임", "https://avatars.slack-edge.com/2020-01-17/900291967601_063326588d6eff8f814a_192.png"))

        conversationRepository.save(Conversation("1", persistMember, toLocalDateTime("1588828683.270200")))
        conversationRepository.save(Conversation("2", persistMember, toLocalDateTime("1588828683.270201")))
        val pivot = conversationRepository.save(Conversation("3", persistMember, toLocalDateTime("1588828683.270202")))
        conversationRepository.save(Conversation("4", persistMember, toLocalDateTime("1588828683.270203")))
        val pageable: Pageable = PageRequest.of(0, 2);

        val conversations = conversationRepository.findByMemberNameOrMessageBeforeTimeOrderDesc(pivot.conversationTime, message, pageable)

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

    @Test
    fun `이름으로 잘 검색되는지 확인`() {
        val persistMember1 = memberRepository.save(Member("SJFV7ENCV", "닉네임1", "https://avatars.slack-edge.com/2020-01-17/900291967601_063326588d6eff8f814a_192.png"))
        val persistMember2 = memberRepository.save(Member("USU9TR4HM", "닉네임2", "https://avatars.slack-edge.com/2020-01-17/900291967601_063326588d6eff8f814a_192.png"))

        conversationRepository.save(Conversation("하나", persistMember1, toLocalDateTime("1588828683.270200")))
        conversationRepository.save(Conversation("둘", persistMember2, toLocalDateTime("1588828683.270201")))
        val conversation3 = conversationRepository.save(Conversation("셋", persistMember1, toLocalDateTime("1588828683.374934")))
        conversationRepository.save(Conversation("넷", persistMember2, toLocalDateTime("1588828683.875342")))
        val pageable: Pageable = PageRequest.of(0, 4)

        val conversations = conversationRepository.findByMemberNameOrMessageBeforeTimeOrderDesc(LocalDateTime.now(), "닉네임1", pageable)

        assertAll(
            { assertThat(conversations.content).hasSize(2) },
            { assertThat(conversations.content[0].message).isEqualTo(conversation3.message) }
        )
    }
}
