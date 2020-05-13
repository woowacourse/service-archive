package camp.nextstep.archive

import camp.nextstep.slack.DateTimeConverter.toLocalDateTime
import camp.nextstep.slack.Message
import camp.nextstep.slack.SlackService
import org.springframework.stereotype.Service
import java.util.stream.Collectors.toList

const val INITIAL_INDEX = 1

@Service
class ConversationService(
        private val repository: ConversationRepository,
        private val slackService: SlackService
) {
    fun archive(): List<Conversation> {
        val conversations = slackService.retrieve()
                .conversations
                .stream()
                .map { to(it) }
                .collect(toList())

        return repository.saveAll(conversations)
    }

    fun retrieve(): List<Conversation> = repository.findAll()

    private fun to(it: camp.nextstep.slack.Conversation): Conversation {
        val conversation = Conversation(it.message, it.user, toLocalDateTime(it.ts))
        conversation.addAll(assemble(conversation, it.thread.messages))
        return conversation
    }

    private fun assemble(conversation: Conversation, messages: List<Message>): MutableList<Reply> {
        return messages
                .subList(INITIAL_INDEX, messages.size)
                .stream()
                .map { assemble(conversation, it) }
                .collect(toList())
    }

    private fun assemble(conversation: Conversation, message: Message): Reply {
        return Reply(conversation, message.text, message.user, toLocalDateTime(message.ts))
    }
}
