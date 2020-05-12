package camp.nextstep.slack

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class User(
        @JsonProperty("ok")
        val status: String,

        @JsonProperty("members")
        val members: List<Member> = mutableListOf()
) : Slack {
    override fun exist() = status == CONDITION_TRUE
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class Member(
        val id: String,

        val name: String
)
