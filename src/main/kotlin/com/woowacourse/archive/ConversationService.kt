package com.woowacourse.archive

import com.woowacourse.slack.Conversations
import com.woowacourse.slack.DateTimeConverter.toLocalDateTime
import com.woowacourse.slack.Message
import com.woowacourse.slack.SlackService
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.util.stream.Collectors.toList

const val INITIAL_INDEX = 1

private val logger = KotlinLogging.logger { }

@Service
class ConversationService(
        private val repository: ConversationRepository,
        private val slackService: SlackService
) {
    fun archive(): List<Conversation> = save(retrieve())

    fun retrieve(): List<Conversation> = repository.findAllByOrderByConversationTime()

    private fun save(savedConversations: List<Conversation>): MutableList<Conversation> {
        if (savedConversations.isEmpty()) {
            return saveAll(slackService.retrieve())
        }
        return saveAll(slackService.retrieve(getLastConversationTime(savedConversations)))
    }

    private fun getLastConversationTime(savedConversations: List<Conversation>) =
            savedConversations[savedConversations.size - 1].conversationTime.toString()

    private fun saveAll(conversations: Conversations): MutableList<Conversation> =
            repository.saveAll(toList(conversations))

    private fun toList(conversations: Conversations): List<Conversation> {
        return conversations
                .conversations.stream()
                .map { to(it) }
                .collect(toList())
    }

    private fun to(it: com.woowacourse.slack.Conversation): Conversation {
        val conversation = Conversation(
                it.message,
                it.user,
                toLocalDateTime(it.ts)
        )
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
        return Reply(
                conversation,
                message.text,
                message.user,
                toLocalDateTime(message.ts)
        )
    }
}
