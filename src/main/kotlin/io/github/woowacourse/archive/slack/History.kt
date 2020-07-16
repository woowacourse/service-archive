package io.github.woowacourse.archive.slack

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class History(
    @JsonProperty("ok")
    val status: String,
    @JsonProperty("messages")
    val messages: List<Message> = mutableListOf()
) : Slack {
    override fun exist() = status == CONDITION_TRUE
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class Message(
    val text: String,
    val user: String = "",
    val ts: String,
    val files: List<File> = emptyList()
) {
    override fun toString(): String {
        return "user: $user, text: $text\n"
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class Conversations(
    val conversations: MutableList<Conversation> = mutableListOf()
) {
    fun add(message: Message, history: History) {
        if (history.exist()) {
            conversations.add(
                Conversation(
                    message.text,
                    message.user,
                    message.ts,
                    message.files,
                    history
                )
            )
        }
    }

    fun exist(): Boolean = conversations.isNotEmpty()
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class Conversation(
    val message: String,
    val user: String,
    val ts: String,
    val files: List<File>,
    val thread: History
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class File(
    val id: String,
    val name: String,
    @JsonProperty("url_private")
    val urlPrivate: String
) {
    override fun toString(): String {
        return "id: $id, name: $name, url: $urlPrivate"
    }
}