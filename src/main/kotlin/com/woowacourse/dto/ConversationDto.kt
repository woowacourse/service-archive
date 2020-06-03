package com.woowacourse.dto

import com.woowacourse.archive.Conversation
import com.woowacourse.archive.Reply
import java.time.LocalDateTime

data class ConversationDto(
    val id: Long,
    val message: String,
    val userId: String,
    val conversationTime: LocalDateTime,
    val replies: List<ReplyDto>
) {
    constructor(conversation: Conversation) :
            this(
                conversation.id,
                conversation.message,
                conversation.userId,
                conversation.conversationTime,
                ReplyDto.listOf(conversation.replies)
            )

    companion object {
        fun listOf(conversations: List<Conversation>): List<ConversationDto> {
            return conversations.map { ConversationDto(it) }
        }
    }
}

data class ReplyDto(
    val message: String,
    val userId: String,
    val replyTime: LocalDateTime,
    val id: Long
) {
    constructor(reply: Reply) :
            this(
                reply.message,
                reply.userId,
                reply.replyTime,
                reply.id
            )

    companion object {
        fun listOf(replies: List<Reply>): List<ReplyDto> {
            return replies.map { ReplyDto(it) }
        }
    }
}
