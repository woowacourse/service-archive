package camp.nextstep.slack

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class History(
        @JsonProperty("ok")
        val status: String,

        @JsonProperty("messages")
        val messages: List<Message> = mutableListOf()
) : Slack{
    override fun exist() = status == CONDITION_TRUE
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class Message(
        val text: String,

        val user: String = "",

        val ts: String
) {
    override fun toString(): String {
        return "user: $user, text: $text\n"
    }
}

data class Conversations(
        val conversations: MutableList<Conversation> = mutableListOf()
) {
    fun add(message: Message, history: History) {
        if (history.exist()) {
            conversations.add(Conversation(message.text, message.user, message.ts, history))
        }
    }

    fun exist(): Boolean = conversations.isNotEmpty()
}

data class Conversation(
        val message: String,
        val user: String,
        val ts: String,
        val thread: History
)
