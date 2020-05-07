package camp.nextstep.archive

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SlackRepositoryTest {

    @Autowired
    lateinit var slackRepository: SlackRepository

    val token = ""
    val channel = ""

    @Test
    fun `Slack 특정 채널의 히스토리를 조회한다`() {
        val response = slackRepository.request(UrlFormatter.make("conversations.history", token, channel))

        assertThat(response).isNotNull
    }

    @Test
    fun `Slack 특정 채널의 특정 시간대 Thread를 조회한다`() {
        val history = slackRepository.request(UrlFormatter.make("conversations.history", token, channel))
        val answers = slackRepository.request(UrlFormatter.make("conversations.replies", token, channel, history.messages[0].ts))

        assertThat(answers).isNotNull
    }

    @Test
    fun `Slack 특정 채널의 히스토리(Thread 포함)를 조회한다`() {
        val response = slackRepository.retrieve(token, channel)

        assertThat(response).isNotNull
    }
}
