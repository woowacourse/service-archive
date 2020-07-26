package io.github.woowacourse.archive.member

import io.github.woowacourse.archive.conversation.BaseAuditEntity
import javax.persistence.*

@Entity
class Member(
    @Column(unique = true, nullable = false)
    val memberId: String,
    val displayName: String,
    @Lob
    val avatar: String,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
) : BaseAuditEntity()