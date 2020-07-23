package io.github.woowacourse.archive.slack

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.net.URLConnection

@Configuration
@ConfigurationProperties("slack")
class SlackProperties {
    lateinit var tokenHeader: String
    lateinit var userToken: String
    lateinit var tokenType: String
    lateinit var botToken: String
    lateinit var channel: String
}

@Service
class SlackService(
    private val repository: SlackRepository,
    private val properties: SlackProperties
) {
    companion object {
        private const val EOF = 0
        private const val START_OFFSET = 0
        private const val BUFFER_SIZE = 8192
    }

    fun retrieve() = repository.retrieve(properties.userToken, properties.channel)

    fun retrieve(oldest: String) =
        repository.retrieve(properties.userToken, properties.channel, oldest)

    fun download(url: String, fileName: String): File {
        val inputStream = connect(url).getInputStream()
        val file = File(fileName)
        val outputStream = FileOutputStream(file)
        val bytes = ByteArray(BUFFER_SIZE)
        var read: Int
        while (inputStream.read(bytes).also { read = it } >= EOF) {
            outputStream.write(bytes, START_OFFSET, read)
        }

        return file
    }

    private fun connect(url: String): URLConnection {
        val connection: URLConnection = URL(url).openConnection()
        connection.setRequestProperty(
            properties.tokenHeader,
            "${properties.tokenType} ${properties.userToken}"
        )
        return connection
    }
}
