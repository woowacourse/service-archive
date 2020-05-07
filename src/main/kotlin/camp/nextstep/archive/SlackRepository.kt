package camp.nextstep.archive

import ch.qos.logback.core.CoreConstants.EMPTY_STRING
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

    private fun retrieveAnswers(history: History, token: String, channel: String): Qnas {
        val qnas = Qnas()
        history.messages.forEach {
            qnas.add(it, request(UrlFormatter.make("conversations.replies", token, channel, it.ts)))
        }
        return qnas
    }

    private fun request(url: String): History {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED
        val body = slackRest.request(HttpMethod.GET, url, null, headers).body
        return jacksonObjectMapper().readValue(body, History::class.java)

    }
}

object UrlFormatter {
    fun make(api: String, token: String, channel: String, ts: String = EMPTY_STRING): String {
        if (ts.isNullOrBlank()) {
            return "https://slack.com/api/${api}?token=${token}&channel=${channel}"
        }
        return "https://slack.com/api/${api}?token=${token}&channel=${channel}&ts=${ts}"
    }
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
