package io.github.woowacourse.archive.config

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class S3Client {
    @Value("\${cloud.aws.region.static}")
    lateinit var region: String

    @Bean
    fun getS3Client(): AmazonS3 {
        return AmazonS3ClientBuilder.standard()
            .withRegion(region)
            .build()
    }
}
