package io.github.woowacourse.archive.member

data class MemberDto(
    val id: Long,
    val displayName: String,
    val avatar: String,
    val memberId: String
) {
    constructor(member: Member) :
        this(
            member.id,
            member.displayName,
            member.avatar,
            member.memberId
        )
}