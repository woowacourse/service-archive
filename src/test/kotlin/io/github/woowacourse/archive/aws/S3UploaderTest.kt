package io.github.woowacourse.archive.aws

import io.github.woowacourse.archive.conversation.IntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.io.File

internal class S3UploaderTest @Autowired constructor(
    private val s3Uploader: S3Uploader
) : IntegrationTest() {
    @Autowired
    private lateinit var cloudProperties: CloudProperties

    @Test
    fun `S3 image upload test`() {
        val filePath = "src/test/resources"
        val fileName = "upload_image.png"
        val file = File("$filePath/$fileName")
        val expected = "https://${cloudProperties.bucket}.s3.ap-northeast-2.amazonaws.com/$fileName"

        val actual = s3Uploader.upload(file, fileName)
        assertThat(actual).contains(expected)
    }
}
