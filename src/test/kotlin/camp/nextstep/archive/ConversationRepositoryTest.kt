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
) : BaseArchiveTest() {

    @Test
    fun `Reply 객체가 저장하는지 확인`() {
        conversation.add(assemble(messages[0]))
        conversation.add(assemble(messages[1]))

        val expected = conversationRepository.save(conversation)
        assertThat(expected.id).isEqualTo(1L)

        val actual = conversationRepository.findById(expected.id).orElseThrow { throw NoSuchElementException("객체를 찾을 수 없습니다.") }
        assertThat(actual).isEqualTo(expected)
        assertThat(actual.replies.size).isEqualTo(messages.size)
    }
}