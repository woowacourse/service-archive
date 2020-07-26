package io.github.woowacourse.archive.member

import io.github.woowacourse.archive.conversation.IntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

internal class MemberServiceTest @Autowired constructor(
    private val memberService: MemberService
) : IntegrationTest() {

    @Test
    @Disabled
    fun `사용자 저장 테스트`() {
        memberService.save()
        assertThat(memberService.findAll()).isNotEmpty
    }
}