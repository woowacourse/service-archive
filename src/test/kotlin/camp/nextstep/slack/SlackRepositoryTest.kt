package camp.nextstep.slack

import camp.nextstep.slack.DateTimeConverter.toLocalDateTime
import camp.nextstep.slack.DateTimeConverter.toTimestamp
import camp.nextstep.slack.Mapper.toHistory
import camp.nextstep.slack.UrlFormatter.make
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.util.*

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SlackRepositoryTest {
//TODO: Property를 별도로 관리한다.
//TODO: 예외처리를 추가한다.
    @Autowired
    lateinit var slackRepository: SlackRepository

    @Autowired
    lateinit var properties: SlackProperties

    lateinit var botToken: String
    lateinit var userToken: String
    lateinit var channel: String

    @BeforeEach
    fun setUp() {
        botToken = properties.botToken
        userToken = properties.userToken
        channel = properties.channel
    }

    @Test
    fun `URL QueryParam을 매핑한다`() {
        val defaultUrl = make(API_HISTORY, "token-value")
        assertThat(defaultUrl).isEqualTo("$HOST$API_HISTORY?token=token-value")

        val addChannel = make(API_HISTORY, "token-value", channel = "channel-value")
        assertThat(addChannel).isEqualTo("$HOST$API_HISTORY?token=token-value&channel=channel-value")

        val addTs = make(API_HISTORY, "token-value", channel = "channel-value", ts = "ts-value")
        assertThat(addTs).isEqualTo("$HOST$API_HISTORY?token=token-value&channel=channel-value&ts=ts-value")
    }

    @Test
    fun `Slack 특정 채널의 히스토리를 조회한다`() {
        val response = toHistory(slackRepository.request(make(API_HISTORY, userToken, channel = channel)))

        assertThat(response.exist()).isTrue()
    }

    @Test
    fun `특정 시점 이후의 Slack 특정 채널의 히스토리를 조회한다`() {
        val secondMessage = toHistory(slackRepository.request(make(API_HISTORY, userToken, channel = channel))).messages[1]
        val latest = toHistory(slackRepository.request(make(API_HISTORY, userToken, channel = channel, oldest = secondMessage.ts)))

        assertThat(latest.exist()).isTrue()
        assertThat(latest.messages.size).isEqualTo(1)
    }

    @Test
    fun `Slack 특정 채널의 특정 시간대 Thread를 조회한다`() {
        val history = toHistory(slackRepository.request(make(API_HISTORY, userToken, channel = channel)))
        val answers = toHistory(slackRepository.request(make(API_REPLY, userToken, channel, history.messages[0].ts)))

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
        val actual = toLocalDateTime(timeStamp)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `LocalDateTime to Slack Timestamp`() {
        val datetime = "2020-05-11T18:43:05.365"
        val expected = "1589190185.999999"

        val actual = toTimestamp(datetime)
        assertThat(actual).isEqualTo(expected)
    }
}
