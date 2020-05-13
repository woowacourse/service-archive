package camp.nextstep.slack

import org.springframework.stereotype.Service

@Service
class SlackService(
        private val repository: SlackRepository
) {
    val token = ""
    val channel = ""

    fun retrieve() = repository.retrieve(token, channel)
    fun retrieve(oldest: String) = repository.retrieve(token, channel, oldest)
}
