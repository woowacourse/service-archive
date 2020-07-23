package io.github.woowacourse.archive.slack

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

    val name: String,

    @JsonProperty("profile")
    val profile: Profile
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Profile(
    @JsonProperty("real_name")
    val realName: String,

    @JsonProperty("display_name")
    val displayName: String,

    @JsonProperty("image_192")
    val profileImage: String
)
