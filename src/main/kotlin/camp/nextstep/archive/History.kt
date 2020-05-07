package camp.nextstep.archive

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class History(
        @JsonProperty("ok")
        var status: String,

        @JsonProperty("messages")
        val messages: List<Message> = mutableListOf()
) {
    fun exist() = status == "true"
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class Message(
        val text: String,

        val user: String = "",

        val ts: String
) {
    override fun toString(): String {
        return "$text\n"
    }
}

data class Qnas(
        val qnas: MutableList<Qna> = mutableListOf()
) {
    fun add(question: Message, answers: History) {
        if (answers.exist()) {
            qnas.add(Qna(question.text, question.user, question.ts, answers))
        }
    }
}


data class Qna(
        val question: String,
        val user: String,
        val ts: String,
        val answer: History
)
