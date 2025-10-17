package es.unizar.webeng.lab2

import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

/**
 * Data Transfer Object (DTO) representing the current server time.
 *
 * @property time The current time provided by the server.
 */
data class TimeDTO(
    val time: LocalDateTime,
)

/**
 * Defines a provider capable of returning the current time.
 */
interface TimeProvider {
    /**
     * Returns the current date and time.
     *
     * @return A [LocalDateTime] representing the current moment.
     */
    fun now(): LocalDateTime
}

/**
 * Default implementation of [TimeProvider] that uses the system clock.
 */
@Service
class TimeService : TimeProvider {
    /**
     * Retrieves the current system time.
     *
     * @return The current [LocalDateTime].
     */
    override fun now(): LocalDateTime = LocalDateTime.now()
}

/**
 * Converts a [LocalDateTime] instance into a [TimeDTO].
 *
 * @return A [TimeDTO] containing this date-time value.
 */
fun LocalDateTime.toDTO(): TimeDTO = TimeDTO(time = this)

/**
 * REST controller that exposes the `/time` endpoint.
 *
 * The endpoint returns the current server time as a JSON object.
 *
 * Example response:
 * ```json
 * { "time": "2025-10-17T19:23:45.123" }
 * ```
 *
 * @property service The [TimeProvider] used to obtain the current time.
 */
@RestController
class TimeController(
    private val service: TimeProvider,
) {
    /**
     * Handles GET requests to `/time` and returns the current server time.
     *
     * @return A [TimeDTO] representing the current time.
     */
    @GetMapping("/time")
    fun time(): TimeDTO = service.now().toDTO()
}
