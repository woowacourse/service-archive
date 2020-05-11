package camp.nextstep.archive

import camp.nextstep.slack.DateTimeConverter
import camp.nextstep.slack.Message
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import javax.transaction.Transactional

@SpringBootTest
@Transactional
class ConversationRepositoryTest @Autowired constructor(
        val conversationRepository: ConversationRepository
) {
    @Test
    fun `Conversation, Reply 객체가 저장하는지 확인`() {
        val message = "상태를 가지지 않아야 한다는 부분이 헷갈리는데요, 도메인 객체들이 스프링 빈 객체여야 하는건 아니죠..?"
        val userId = "USU9TR4HM"
        val conversationTime = DateTimeConverter.toLocalDateTime("1588828683.270200")
        val conversation = Conversation(message, userId, conversationTime)

        conversation.add(to(conversation, Message("답변", "USDLAAJBU", "1588828683.270200")))
        conversation.add(to(conversation, Message("답변2", "USDLAABCD", "1588828684.270200")))

        val expected = conversationRepository.save(conversation)
        assertThat(expected.id).isEqualTo(1L)

        val actual = conversationRepository.findById(expected.id)
        assertThat(actual.get()).isEqualTo(expected)

        assertThat(actual.get().replies.size).isEqualTo(2)
    }

    fun to(conversation: Conversation, message: Message): Reply {
        return Reply(conversation, message.text, message.user, DateTimeConverter.toLocalDateTime(message.ts))
    }
}
