package io.github.woowacourse.archive.slack

import io.github.woowacourse.archive.conversation.IntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.io.File

internal class SlackServiceTest : IntegrationTest() {
    @Autowired
    lateinit var slackService: SlackService

    @Test
    internal fun `로컬 파일을 다운로드한다`() {
        val origin = File("src/test/resources/upload_image.png")
        val downloaded = slackService.download(origin.toURL().toString(), "target.png")

        assertThat(downloaded.length()).isEqualTo(origin.length())
    }

    @Test
    internal fun `Slack 채널의 대화 중 파일이 있을 경우, 파일을 다운로드한다`() {
        val urls = retrieveFileUrls()

        if (urls.isNotEmpty()) {
            val file = slackService.download(urls[0], "test_image.png")
            assertThat(file.exists()).isTrue()
            file.delete()
        }
    }

    private fun retrieveFileUrls(): List<String> {
        return slackService
            .retrieve()
            .conversations
            .flatMap { conversation ->
                conversation.files.map { it.urlPrivate }
            }
    }
}
