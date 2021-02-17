package io.github.woowacourse.archive.member
import io.github.woowacourse.archive.aws.S3Uploader
import io.github.woowacourse.archive.slack.SlackService
import io.github.woowacourse.archive.slack.User
import org.springframework.stereotype.Service
import java.io.File

@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val slackService: SlackService,
    private val s3Uploader: S3Uploader
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
        val fileName: String =
            if (member.profile.profileImage.contains(".png")) "${member.id}.png" else "${member.id}.jpg"
//        val imageFile: File = slackService.download(member.profile.profileImage, fileName)
//        val avatarUrl: String = s3Uploader.upload(imageFile, "avatar/${displayName}")
        return Member(member.id, displayName, "")
    }

    fun findAll(): List<Member> {
        return memberRepository.findAll()
    }
}
