package camp.nextstep.slack

import camp.nextstep.Rest
import camp.nextstep.slack.DataMapper.read
import ch.qos.logback.core.CoreConstants.EMPTY_STRING
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.*
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import java.sql.Timestamp
import java.time.LocalDateTime

private val logger = KotlinLogging.logger { }

@Service
class SlackRepository {

    @Autowired
    lateinit var slackRest: SlackRest

    fun retrieve(token: String, channel: String): Conversations {
        val history = request(UrlFormatter.make("conversations.history", token, channel))
        return retrieveAnswers(history, token, channel)
    }

    fun retrieveAnswers(history: History, token: String, channel: String): Conversations {
        val conversations = Conversations()
        history.messages.forEach {
            conversations.add(it, request(UrlFormatter.make("conversations.replies", token, channel, it.ts)))
        }
        return conversations
    }

    fun request(url: String): History {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED
        return read(slackRest.request(HttpMethod.GET, url, null, headers).body)
    }
}

object DateTimeConverter {
    private const val UNNECESSARY_CHAR: String = "."
    private const val SLACK_TIMESTAMP_LENGTH = 13

    fun toLocalDateTime(timestamp: String): LocalDateTime = Timestamp(convert(timestamp)).toLocalDateTime()

    private fun convert(timestamp: String): Long =
            timestamp
                    .replace(UNNECESSARY_CHAR, "")
                    .substring(0, SLACK_TIMESTAMP_LENGTH).toLong()
}

object UrlFormatter {
    fun make(api: String, token: String, channel: String, ts: String = EMPTY_STRING): String {
        val url = "https://slack.com/api/${api}?token=${token}&channel=${channel}"
        if (ts.isNullOrBlank()) {
            return url
        }
        return "${url}&ts=${ts}"
    }
}

object DataMapper {
    private val mapper: ObjectMapper = jacksonObjectMapper()

    fun read(body: String?): History = mapper.readValue(body, History::class.java)
}

@Component
class SlackRest : Rest<MultiValueMap<String, String>> {
    override fun request(method: HttpMethod,
                         url: String,
                         contents: MultiValueMap<String, String>?,
                         headers: HttpHeaders): ResponseEntity<String> {
        logger.debug("request url : {}, params : {}", url, contents)
        return RestTemplate().exchange(
                url,
                method,
                HttpEntity(contents, headers),
                String::class.java
        )
    }
}
