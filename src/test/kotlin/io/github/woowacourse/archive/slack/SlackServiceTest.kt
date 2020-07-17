package io.github.woowacourse.archive.slack

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.io.File

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class SlackServiceTest {
    @Autowired
    lateinit var slackService: SlackService

    @Test
    internal fun `로컬 파일을 다운로드한다`() {
        val file = File("src/test/resources/upload_image.png")
        val downloaded =
            slackService.download(
                File("src/test/resources/upload_image.png").toURI().toURL().toString(),
                "test_image.png"
            )

        assertThat(downloaded.length()).isEqualTo(file.length())
    }


    @Test
    internal fun `Slack의 파일을 다운로드한다`() {
        val urls = slackService.retrieve()
            .conversations.flatMap { conversation -> conversation.files.map { it.urlPrivate } }

        when {
            urls.isNotEmpty() -> {
                val file =
                    slackService.download(
                        urls[0],
                        "test_image.png"
                    )

                assertThat(file.exists()).isTrue()
                file.delete()
            }
        }
    }
}
