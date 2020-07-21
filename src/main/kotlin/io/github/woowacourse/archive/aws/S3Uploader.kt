package io.github.woowacourse.archive.aws

import com.amazonaws.regions.Regions.AP_NORTHEAST_2
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.PutObjectRequest
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import java.io.File
import java.util.*

@Configuration
@ConfigurationProperties("cloud.aws.s3")
class CloudProperties {
    lateinit var bucket: String
}

@Component
class S3Uploader(
        private val properties: CloudProperties
) {
    private val amazonS3 = AmazonS3ClientBuilder
            .standard()
            .withRegion(AP_NORTHEAST_2)
            .build()

    fun upload(file: File, path: String = getFilePath()): String {
        amazonS3.putObject(
                PutObjectRequest(properties.bucket, path, file)
                        .withCannedAcl(CannedAccessControlList.PublicRead)
        )
        return amazonS3.getUrl(properties.bucket, path).toString()
    }

    private fun getFilePath(): String = UUID.randomUUID().toString().replace("-", "")
}
