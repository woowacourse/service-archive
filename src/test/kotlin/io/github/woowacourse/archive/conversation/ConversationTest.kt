package io.github.woowacourse.archive.conversation

import io.github.woowacourse.archive.member.Member
import io.github.woowacourse.archive.slack.DateTimeConverter.toLocalDateTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ConversationTest {
    private val message = "상태를 가지지 않아야 한다는 부분이 헷갈리는데요, 도메인 객체들이 스프링 빈 객체여야 하는건 아니죠..?"
    private val userId = "USU9TR4HM"
    private val conversationTime = toLocalDateTime("1588828683.270200")

    @Test
    fun `대화 객체를 생성한다`() {
        val conversation = Conversation(
                message,
                Member(userId, "닉네임","https://avatars.slack-edge.com/2020-01-17/900291967601_063326588d6eff8f814a_192.png"),
                conversationTime
        )

        assertThat(conversation.message).isNotNull()
    }

    @Test
    fun `대화에 응답을 추가한다`() {
        val conversation = Conversation(
                message,
                Member(userId, "닉네임","https://avatars.slack-edge.com/2020-01-17/900291967601_063326588d6eff8f814a_192.png"),
                conversationTime
        )
        val text = "답변"
        val user = "USDLAAJBU"
        val ts = "1588828683.270200"
        conversation.add(
                Reply(
                        conversation,
                        text,
                        Member(user, "닉네임","https://avatars.slack-edge.com/2020-01-17/900291967601_063326588d6eff8f814a_192.png"),
                        toLocalDateTime(ts)
                )
        )

        assertThat(conversation.replies.size).isEqualTo(1)
    }
}
