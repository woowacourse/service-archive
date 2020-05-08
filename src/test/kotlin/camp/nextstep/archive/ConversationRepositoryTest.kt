package camp.nextstep.archive

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import javax.transaction.Transactional

@SpringBootTest
@Transactional
class ConversationRepositoryTest @Autowired constructor(
        val conversationRepository: ConversationRepository
){
    @Test
    fun `Conversation 객체를 저장하는지 확인`() {
        val message = "상태를 가지지 않아야 한다는 부분이 헷갈리는데요, 도메인 객체들이 스프링 빈 객체여야 하는건 아니죠..?"
        val userId = "USU9TR4HM"
        val conversation = Conversation(message, userId)
        val expected = conversationRepository.save(conversation)
        assertThat(expected.id).isEqualTo(1L)

        val actual = conversationRepository.findById(expected.id)
        assertThat(actual.get()).isEqualTo(expected)
    }
}
