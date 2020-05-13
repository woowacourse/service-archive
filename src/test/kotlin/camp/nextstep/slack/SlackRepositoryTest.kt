package camp.nextstep.slack

import camp.nextstep.slack.Mapper.toHistory
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SlackRepositoryTest {
//TODO: Property를 별도로 관리한다.
//TODO: 예외처리를 추가한다.
    @Autowired
    lateinit var slackRepository: SlackRepository

    val botToken = ""
    val userToken = ""
    val channel = ""

    @Test
    fun `URL QueryParam을 매핑한다`() {
        val defaultUrl = UrlFormatter.make(API_HISTORY, "token-value")
        assertThat(defaultUrl).isEqualTo("$HOST$API_HISTORY?token=token-value")

        val addChannel = UrlFormatter.make(API_HISTORY, "token-value", channel = "channel-value")
        assertThat(addChannel).isEqualTo("$HOST$API_HISTORY?token=token-value&channel=channel-value")

        val addTs = UrlFormatter.make(API_HISTORY, "token-value", channel = "channel-value", ts = "ts-value")
        assertThat(addTs).isEqualTo("$HOST$API_HISTORY?token=token-value&channel=channel-value&ts=ts-value")
    }

    @Test
    fun `Slack 특정 채널의 히스토리를 조회한다`() {
        val response = toHistory(slackRepository.request(UrlFormatter.make(API_HISTORY, userToken, channel = channel)))

        assertThat(response.exist()).isTrue()
    }

    @Test
    fun `Slack 특정 채널의 특정 시간대 Thread를 조회한다`() {
        val history = toHistory(slackRepository.request(UrlFormatter.make(API_HISTORY, userToken, channel = channel)))
        val answers = toHistory(slackRepository.request(UrlFormatter.make(API_REPLY, userToken, channel, history.messages[0].ts)))

        assertThat(answers.exist()).isTrue()
    }

    @Test
    fun `Slack 특정 채널의 히스토리(Thread 포함)를 조회한다`() {
        val response = slackRepository.retrieve(userToken, channel)

        assertThat(response.exist()).isTrue()
    }

    @Test
    fun `Slack User 리스트를 조회한다`() {
        val response = slackRepository.retrieveUsers(botToken)

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
