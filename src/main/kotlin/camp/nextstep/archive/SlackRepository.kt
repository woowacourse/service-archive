package camp.nextstep.archive

import camp.nextstep.archive.DataMapper.read
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

private val logger = KotlinLogging.logger { }

@Service
class SlackRepository {

    @Autowired
    lateinit var slackRest: SlackRest

    fun retrieve(token: String, channel: String): Any {
        val history = request(UrlFormatter.make("conversations.history", token, channel))
        return retrieveAnswers(history, token, channel)
    }

    fun retrieveAnswers(history: History, token: String, channel: String): Qnas {
        val qnas = Qnas()
        history.messages.forEach {
            qnas.add(it, request(UrlFormatter.make("conversations.replies", token, channel, it.ts)))
        }
        return qnas
    }

    fun request(url: String): History {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED
        return read(slackRest.request(HttpMethod.GET, url, null, headers).body)
    }
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
