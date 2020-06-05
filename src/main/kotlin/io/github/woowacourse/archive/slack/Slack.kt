package io.github.woowacourse.archive.slack

const val CONDITION_TRUE = "true"

interface Slack {
    fun exist(): Boolean
}
