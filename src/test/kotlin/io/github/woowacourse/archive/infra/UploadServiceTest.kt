package io.github.woowacourse.archive.infra

import io.github.woowacourse.archive.conversation.IntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.io.File

internal class UploadServiceTest @Autowired constructor(
    private val uploadService: UploadService
) : IntegrationTest() {
    @Test
    fun `S3 이미지 upload 테스트`() {
        val uploadedUrl = uploadService.upload(File("./upload_image.png"), "static")
        assertThat(uploadedUrl).contains("amazonaws.com")
    }
}