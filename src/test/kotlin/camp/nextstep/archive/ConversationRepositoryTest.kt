package camp.nextstep.archive

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

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
}
