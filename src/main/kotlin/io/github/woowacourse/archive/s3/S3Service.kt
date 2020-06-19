package io.github.woowacourse.archive.s3

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.PutObjectRequest
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URL

@Service
class S3Service(
        val amazonS3: AmazonS3
) {
    fun upload(url: String, dirName: String, fileName: String): String {
        val file: File = File(fileName)
        copyInputStreamToFile(URL(url).openStream(), file)
        return upload(file, dirName)
    }

    private fun copyInputStreamToFile(inputStream: InputStream, file: File) {
        val outputStream: FileOutputStream = FileOutputStream(file)
        val bytes: ByteArray = ByteArray(8192)
        var read: Int = 0

        while (read != -1) {
            outputStream.write(bytes, 0, read)
            read = inputStream.read(bytes)
        }
    }

    private fun upload(file: File, dirName: String): String {
        val filePath: String = dirName + "/" + file.name
        val uploadUrl: String = save(file, filePath)
        remove(file)
        return uploadUrl
    }

    private fun save(file: File, filePath: String): String {
        amazonS3.putObject(PutObjectRequest("archive-test-storage", filePath, file).withCannedAcl(
                CannedAccessControlList.PublicRead
        ))
        return amazonS3.getUrl("archive-test-storage", filePath).toString()
    }

    private fun remove(file: File) {
        file.delete()
    }
}
