package camp.nextstep.slack

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service

@Configuration
@ConfigurationProperties("slack")
class SlackProperties {
    lateinit var userToken: String
    lateinit var botToken: String
    lateinit var channel: String
}

@Service
class SlackService(
        private val repository: SlackRepository,
        private val properties: SlackProperties
) {
    fun retrieve() = repository.retrieve(properties.userToken, properties.channel)
    fun retrieve(oldest: String) = repository.retrieve(properties.userToken, properties.channel, oldest)
}
