package camp.nextstep.http

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity

interface Rest<T> {
    fun request(method: HttpMethod,
                url: String,
                contents: T?,
                headers: HttpHeaders = HttpHeaders()): ResponseEntity<String>
}
