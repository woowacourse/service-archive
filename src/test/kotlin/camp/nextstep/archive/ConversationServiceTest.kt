package camp.nextstep.archive

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ConversationServiceTest @Autowired constructor(
        private val conversationService: ConversationService
){
    @Test
    fun `슬랙 히스토리 저장 테스트`() {
        val expected = conversationService.archive()
        val actual = conversationService.retrieve()

        assertThat(actual).isNotNull()
        assertThat(actual.size).isEqualTo(expected.size)
    }
}
