package io.github.woowacourse.archive.conversation

import io.github.woowacourse.archive.aws.S3Uploader
import io.github.woowacourse.archive.member.MemberRepository
import io.github.woowacourse.archive.slack.*
import io.github.woowacourse.archive.slack.DateTimeConverter.toLocalDateTime
import mu.KotlinLogging
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDateTime

const val INITIAL_INDEX = 1
const val FIRST_MATCH_INDEX = 1
private val REGEX = "<@(U\\S+)>".toRegex()
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
        val members = memberRepository.findAll()
            .map { it.memberId to it.displayName }
            .toMap()

        return conversations
            .conversations.asSequence()
            .map { to(it, members) }
            .toList()
    }

    private fun to(it: io.github.woowacourse.archive.slack.Conversation, members: Map<String, String>): Conversation {
        val conversation = Conversation(
            convertUserIdToDisplayName(it.message, members),
            memberRepository.findByMemberId(it.user)!!,
            toLocalDateTime(it.ts)
//            fromSlackToS3(it.files)
        )
        conversation.addAll(assemble(conversation, it.thread.messages, members))
        return conversation
    }

    private fun fromSlackToS3(files: List<io.github.woowacourse.archive.slack.File>): List<File> {
        val files = downloadFromSlack(files)

        return uploadToS3(files)
    }

    private fun assemble(conversation: Conversation, messages: List<Message>, members: Map<String, String>): MutableList<Reply> {
        return messages
            .subList(INITIAL_INDEX, messages.size)
            .asSequence()
            .map { assemble(conversation, it, members) }
            .toMutableList()
    }

    private fun assemble(conversation: Conversation, message: Message, members: Map<String, String>): Reply {
        return Reply(
            conversation,
            convertUserIdToDisplayName(message.text, members),
            memberRepository.findByMemberId(message.user),
            toLocalDateTime(message.ts)
//            fromSlackToS3(message.files)
        )
    }

    private fun convertUserIdToDisplayName(message: String, members: Map<String, String>): String {
        return REGEX.replace(message) { matchResult -> "@${members.getOrDefault(matchResult.groupValues[FIRST_MATCH_INDEX], matchResult.value)}" }
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
