package io.github.woowacourse.archive.member

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MemberController(
    private val memberService: MemberService
) {
    @GetMapping("/create-members")
    fun archive(): ResponseEntity<Unit> {
        memberService.save()
        return ResponseEntity.ok().build()
    }
}