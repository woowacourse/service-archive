package camp.nextstep.slack

import org.springframework.stereotype.Service

@Service
class SlackService(
        private val repository: SlackRepository
) {
    fun retrieve()  = repository.retrieve("", "")
}
