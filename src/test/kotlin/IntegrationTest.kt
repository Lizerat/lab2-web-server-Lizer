package es.unizar.webeng.lab2

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.within
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.annotation.Import
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

/**
 * Integration tests for the web server application.
 *
 * This test class verifies both the custom 404 error page and the `/time` endpoint
 * over an HTTPS connection. The configuration imports [CustomTestConfig],
 * which provides a [TestRestTemplate] capable of trusting self-signed certificates.
 *
 * The tests run on a random port to avoid conflicts.
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Import(CustomTestConfig::class)
class IntegrationTest {
    /**
     * The randomly assigned port for the embedded test server.
     */
    @LocalServerPort
    private var port: Int = 0

    /**
     * A [TestRestTemplate] configured to accept self-signed HTTPS connections.
     */
    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    /**
     * Verifies that accessing a non-existent endpoint returns the custom 404 error page.
     *
     * The test performs an HTTPS GET request to the root path `/` and asserts that:
     * - The response status is 404 (Not Found)
     * - The HTML body contains the expected error messages and structure
     */
    @Test
    fun `should return custom error 404 page`() {
        val headers =
            HttpHeaders().apply {
                accept = listOf(MediaType.TEXT_HTML)
            }
        val entity = HttpEntity<String>(headers)
        val response =
            restTemplate.exchange(
                "https://localhost:$port",
                HttpMethod.GET,
                entity,
                String::class.java,
            )

        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        assertThat(response.body).contains("<h1>Error: 404</h1>")
        assertThat(response.body).contains("<h2>Page Not Found</h2>")
        assertThat(response.body).contains(
            "<p>Sorry, the page you are looking for does not exist or has been moved.</p>",
        )
    }

    /**
     * Verifies that the `/time` endpoint returns a valid JSON response with the current time.
     *
     * The test performs an HTTPS GET request to `/time` and asserts that:
     * - The response status is 200 (OK)
     * - The content type is `application/json`
     * - The returned timestamp is within 5 seconds of the current system time
     */
    @Test
    fun `time endpoint should return time`() {
        val response = restTemplate.getForEntity("https://localhost:$port/time", TimeDTO::class.java)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.headers.contentType).isEqualTo(MediaType.APPLICATION_JSON)
        assertThat(response.hasBody())

        val body = response.body!!
        val now = LocalDateTime.now()
        assertThat(body.time).isCloseTo(now, within(5, ChronoUnit.SECONDS))
    }
}
