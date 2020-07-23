package io.github.woowacourse.archive.member

import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository :JpaRepository<Member, Long> {
    fun findByMemberId(memberId: String): Member
}
