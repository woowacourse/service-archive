package io.github.woowacourse.archive.conversation

import io.github.woowacourse.archive.member.Member
import io.github.woowacourse.archive.slack.DateTimeConverter.toLocalDateTime
import io.github.woowacourse.archive.slack.Message
import org.junit.jupiter.api.BeforeEach
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import javax.transaction.Transactional

@ActiveProfiles(value = ["test"])
abstract class BaseArchiveTest {
    lateinit var conversation: Conversation
    val messages = arrayListOf<Message>()

    @BeforeEach
    fun setUp() {
        val message = "상태를 가지지 않아야 한다는 부분이 헷갈리는데요, 도메인 객체들이 스프링 빈 객체여야 하는건 아니죠..?"
        val userId = "USU9TR4HM"
        val conversationTime = toLocalDateTime("1588828683.270200")

        conversation = Conversation(
                message,
                Member(userId, "닉네임","https://avatars.slack-edge.com/2020-01-17/900291967601_063326588d6eff8f814a_192.png"),
                conversationTime
        )

        messages.add(
            Message(
                "답변",
                "USDLAAJBU",
                "1588828683.270200"
            )
        )
        messages.add(
            Message(
                "답변2",
                "USDLAABCD",
                "1588828684.270200"
            )
        )
    }

    fun assemble(message: Message): Reply {
        return Reply(
                conversation,
                message.text,
                Member(message.user, "닉네임","https://avatars.slack-edge.com/2020-01-17/900291967601_063326588d6eff8f814a_192.png"),
                toLocalDateTime(message.ts)
        )
    }
}

@Transactional
@SpringBootTest
abstract class IntegrationTest protected constructor() : BaseArchiveTest()

