package io.github.woowacourse.archive.infra

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.PutObjectRequest
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service
import java.io.File

@Configuration
@ConfigurationProperties("cloud.aws.s3")
class CloudProperties {
    lateinit var bucket: String
}

@Service
class UploadService(
    val amazonS3: AmazonS3,
    private val properties: CloudProperties
) {
    fun upload(file: File, dirName: String): String {
        val filePath: String = dirName + "/" + file.name
        val uploadUrl: String = save(file, filePath)
        return uploadUrl
    }

    private fun save(file: File, filePath: String): String {
        amazonS3.putObject(
            PutObjectRequest(properties.bucket, filePath, file).withCannedAcl(
                CannedAccessControlList.PublicRead
            )
        )
        return amazonS3.getUrl(properties.bucket, filePath).toString()
    }
}