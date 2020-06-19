package io.github.woowacourse.archive.s3

import org.junit.jupiter.api.Test

class S3ServiceTest {
    @Test
    fun `업로드 테스트`() {
        val url = "https://user-images.githubusercontent.com/7259103/83963271-9ccbd980-a8df-11ea-8a67-4fe5631c8150.png"
        val s3Service: S3Service = S3Service()
        s3Service.upload(url, "static", "fe5631c8150.png")
    }
}
