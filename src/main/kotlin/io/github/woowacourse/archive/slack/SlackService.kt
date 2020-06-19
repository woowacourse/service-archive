package io.github.woowacourse.archive.slack

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URL
import java.net.URLConnection

@Configuration
@ConfigurationProperties("slack")
class SlackProperties {
    lateinit var userToken: String
    lateinit var botToken: String
    lateinit var channel: String
}

@Service
class SlackService(
        private val repository: SlackRepository,
        private val properties: SlackProperties
) {
    fun retrieve() = repository.retrieve(properties.userToken, properties.channel)
    fun retrieve(oldest: String) =
            repository.retrieve(properties.userToken, properties.channel, oldest)
    fun download(url: String, fileName: String): File {
        val file: File = File(fileName)
        val connection : URLConnection = URL(url).openConnection()
        connection.setRequestProperty("Authorization", "Bearer ${properties.userToken}")
        val response = connection.getInputStream()
        copyInputStreamToFile(response, file)
        return file
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
}
