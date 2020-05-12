package camp.nextstep.slack

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
    fun `URL QueryParam을 매핑한다`() {
        val defaultUrl = UrlFormatter.make("conversations.history", "token-value")
        assertThat(defaultUrl).isEqualTo("https://slack.com/api/conversations.history?token=token-value")

        val addChannel = UrlFormatter.make("conversations.history", "token-value", channel = "channel-value")
        assertThat(addChannel).isEqualTo("https://slack.com/api/conversations.history?token=token-value&channel=channel-value")

        val addTs = UrlFormatter.make("conversations.history", "token-value", channel = "channel-value", ts = "ts-value")
        assertThat(addTs).isEqualTo("https://slack.com/api/conversations.history?token=token-value&channel=channel-value&ts=ts-value")
    }


    @Test
    fun `Slack 특정 채널의 히스토리를 조회한다`() {
        val response = slackRepository.request(UrlFormatter.make("conversations.history", token, channel = channel))

        assertThat(response.exist()).isTrue()
    }

    @Test
    fun `Slack 특정 채널의 특정 시간대 Thread를 조회한다`() {
        val history = slackRepository.request(UrlFormatter.make("conversations.history", token, channel = channel))
        val answers = slackRepository.request(UrlFormatter.make("conversations.replies", token, channel, history.messages[0].ts))

        assertThat(answers.exist()).isTrue()
    }

    @Test
    fun `Slack 특정 채널의 히스토리(Thread 포함)를 조회한다`() {
        val response = slackRepository.retrieve(token, channel)

        assertThat(response.exist()).isTrue()
    }

    @Test
    fun `Slack Timestamp to LocalDateTime`() {
        val timeStamp = "1589190185.365000"
        val expected = "2020-05-11T18:43:05.365"
        val actual = DateTimeConverter.toLocalDateTime(timeStamp)

        assertThat(actual).isEqualTo(expected)
    }
}
