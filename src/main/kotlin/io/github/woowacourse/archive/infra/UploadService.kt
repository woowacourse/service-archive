package io.github.woowacourse.archive.infra

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.PutObjectRequest
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Service
import java.io.File

@Service
@ConfigurationProperties("cloud.aws")
class UploadService(val amazonS3: AmazonS3) {
    fun upload(file: File, dirName: String): String {
        val filePath: String = dirName + "/" + file.name
        val uploadUrl: String = save(file, filePath)
        file.deleteOnExit()
        return uploadUrl
    }

    private fun save(file: File, filePath: String): String {
        amazonS3.putObject(
            PutObjectRequest("archive-test-storage", filePath, file).withCannedAcl(
                CannedAccessControlList.PublicRead
            )
        )
        return amazonS3.getUrl("archive-test-storage", filePath).toString()
    }
}