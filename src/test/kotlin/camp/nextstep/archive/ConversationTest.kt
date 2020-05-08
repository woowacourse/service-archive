package camp.nextstep.archive

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ConversationTest {

    @Test
    fun `대화 객체를 생성한다`() {
        val message = "상태를 가지지 않아야 한다는 부분이 헷갈리는데요, 도메인 객체들이 스프링 빈 객체여야 하는건 아니죠..?"
        val userId = "USU9TR4HM"
        val conversation = Conversation(message, userId)

        assertThat(conversation.message).isNotNull()
    }
}
