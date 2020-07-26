package io.github.woowacourse.archive.conversation

import io.github.woowacourse.archive.aws.S3Uploader
import io.github.woowacourse.archive.member.Member
import io.github.woowacourse.archive.member.MemberRepository
import io.github.woowacourse.archive.slack.Conversations
import io.github.woowacourse.archive.slack.DateTimeConverter.toLocalDateTime
import io.github.woowacourse.archive.slack.Message
import io.github.woowacourse.archive.slack.SlackService
import mu.KotlinLogging
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDateTime

const val INITIAL_INDEX = 1
private val logger = KotlinLogging.logger { }

@Service
class ConversationService(
    private val repository: ConversationRepository,
    private val memberRepository: MemberRepository,
    private val slackService: SlackService,
    private val s3Uploader: S3Uploader
) {
    fun archive(): List<Conversation> = save(retrieve())

    fun retrieve(): List<Conversation> = repository.findAllByOrderByConversationTime()

    fun retrieveBy(id: Long): Conversation = repository.findById(id).orElseThrow { throw NoSuchElementException() }

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
            .conversations.asSequence()
            .map { to(it) }
            .toList()
    }

    private fun to(it: io.github.woowacourse.archive.slack.Conversation): Conversation {
        val conversation = Conversation(
            it.message,
            memberRepository.findByMemberId(it.user)!!,
            toLocalDateTime(it.ts),
            fromSlackToS3(it.files)
        )
        conversation.addAll(assemble(conversation, it.thread.messages))
        return conversation
    }

    private fun findMemberOrId(conversation: io.github.woowacourse.archive.slack.Conversation) : String {
        val member: Member? =memberRepository.findByMemberId(conversation.user)
        return member?.displayName ?: conversation.user
    }

    private fun fromSlackToS3(files: List<io.github.woowacourse.archive.slack.File>): List<File> {
        val files = downloadFromSlack(files)

        return uploadToS3(files)
    }

    private fun assemble(conversation: Conversation, messages: List<Message>): MutableList<Reply> {
        return messages
            .subList(INITIAL_INDEX, messages.size)
            .asSequence()
            .map { assemble(conversation, it) }
            .toMutableList()
    }

    private fun assemble(conversation: Conversation, message: Message): Reply {
        return Reply(
            conversation,
            message.text,
            memberRepository.findByMemberId(message.user)!!,
            toLocalDateTime(message.ts),
            fromSlackToS3(message.files)
        )
    }

    private fun findMemberOrId(message: Message) : String {
        val member: Member? =memberRepository.findByMemberId(message.user)
        return member?.displayName ?: message.user
    }

    private fun uploadToS3(downloadFiles: List<java.io.File>): List<File> {
        return downloadFiles
            .map { File(s3Uploader.upload(it)) }
            .toList()
    }

    private fun downloadFromSlack(files: List<io.github.woowacourse.archive.slack.File>) =
        files.map { slackService.download(it.urlPrivate, it.name) }

    fun retrieveSpecific(conversationTime: LocalDateTime, message: String, size: Int): List<Conversation> {
        val pageable: Pageable = PageRequest.of(0, size)
        return repository.findByConversationTimeLessThanAndMessageContainingOrderByConversationTimeDesc(conversationTime, message, pageable).content
    }
}
