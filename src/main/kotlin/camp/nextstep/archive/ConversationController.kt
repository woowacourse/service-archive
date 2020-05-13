package camp.nextstep.archive

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ConversationController(
        private val conversationService: ConversationService
) {
    @GetMapping("/conversations")
    fun retrieve(): ResponseEntity<List<Conversation>> {
        return ResponseEntity.ok(conversationService.retrieve())
    }

    @GetMapping("/archive")
    fun archiveManually(): ResponseEntity<Unit> {
        conversationService.archive()
        return ResponseEntity.ok().build()
    }
}

