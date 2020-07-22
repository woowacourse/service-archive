package io.github.woowacourse.archive.conversation

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class ConversationController(
        private val conversationService: ConversationService
) {
    @GetMapping("/conversations")
    fun retrieve(): ResponseEntity<List<ConversationDto>> {
        val conversationDtos = ConversationDto.listOf(conversationService.retrieve())
        return ResponseEntity.ok(conversationDtos)
    }

    @PostMapping("/conversations")
    fun archiveManually(): ResponseEntity<Unit> {
        conversationService.archive()
        return ResponseEntity.ok().build()
    }

    @GetMapping("/conversations/search")
    fun retrieve(
            @RequestParam("id") id: Long,
            @RequestParam("size") size: Int
    ): ResponseEntity<List<ConversationDto>> {
        val conversationDtos = ConversationDto.listOf(conversationService.retrieveSpecific(id, size))
        return ResponseEntity.ok(conversationDtos);
    }
}
