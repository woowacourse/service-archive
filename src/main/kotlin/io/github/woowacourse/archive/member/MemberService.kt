package io.github.woowacourse.archive.member

import io.github.woowacourse.archive.slack.SlackService
import io.github.woowacourse.archive.slack.User
import org.springframework.stereotype.Service

@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val slackService: SlackService
) {

    fun save() {
        val members = toList(slackService.retrieveUser())
        memberRepository.saveAll(members)
    }

    fun toList(user: User): List<Member> {
        return user.members
            .asSequence()
            .map { to(it) }
            .toList()
    }

    fun to(member: io.github.woowacourse.archive.slack.Member): Member {
        var displayName = member.profile.displayName
        if (member.profile.displayName.isNullOrBlank()) {
            displayName = member.profile.realName
        }
        return Member(member.id, displayName, member.profile.profileImage)
    }

    fun findAll(): List<Member> {
        return memberRepository.findAll()
    }
}
