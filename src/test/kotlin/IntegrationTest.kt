package es.unizar.webeng.lab2

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.within
import org.assertj.core.data.TemporalUnitOffset
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.bind.Bindable.listOf
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.servlet.function.RequestPredicates.accept
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalUnit

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class IntegrationTest {
    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Test
    fun `should return custom error 404 page`() {
        val headers = HttpHeaders().apply{
            accept = listOf(MediaType.TEXT_HTML)
        }
        val entity = HttpEntity<String>(headers)
        val response = restTemplate.exchange(
            "http://localhost:$port",
            HttpMethod.GET,
            entity,
            String::class.java
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        assertThat(response.body).contains("<h1>Error: 404</h1>")
        assertThat(response.body).contains("<h2>Page Not Found</h2>")
        assertThat(response.body).contains(
            "<p>Sorry, the page you are looking for does not exist or has been moved.</p>"
        )
    }

    @Test
    fun `time endpoint should return time`(){
        val response = restTemplate.getForEntity("http://localhost:$port/time", TimeDTO::class.java)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.headers.contentType).isEqualTo(MediaType.APPLICATION_JSON)
        assertThat(response.hasBody())

        val body = response.body!!
        val now = LocalDateTime.now()
        assertThat(body.time).isCloseTo(now, within(5, ChronoUnit.SECONDS))
    }
}
