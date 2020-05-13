package camp.nextstep.archive

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class ConversationServiceTest @Autowired constructor(
        private val conversationService: ConversationService
) : IntegrationTest() {
    @Test
    fun `슬랙 히스토리 저장 테스트`() {
        val expected = conversationService.archive()
        val actual = conversationService.retrieve()

        assertThat(actual).isNotNull()
        assertThat(actual.size).isEqualTo(expected.size)
    }

    @Test
    fun `동시에 여러번 아카이빙 기능을 사용하더라도 중복하여 저장되지 않는다`() {
        val expected = conversationService.archive()
        conversationService.archive()
        conversationService.archive()
        val actual = conversationService.retrieve()

        assertThat(actual).isNotNull()
        assertThat(actual.size).isEqualTo(expected.size)
    }
}
