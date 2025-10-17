package es.unizar.webeng.lab2

import org.apache.hc.client5.http.impl.classic.HttpClients
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory
import org.apache.hc.core5.http.config.RegistryBuilder
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.function.Supplier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

/**
 * Provides a TestRestTemplate bean configured to trust all SSL certificates.
 *
 * This configuration is intended for integration tests against HTTPS endpoints
 * using self-signed certificates. It ensures that SSL verification does not
 * prevent tests from running successfully.
 */
@Configuration
class CustomTestConfig {
    /**
     * Creates a [TestRestTemplate] that accepts all SSL certificates.
     *
     * A custom SSL context is created with a TrustManager that does not validate
     * certificate chains. The SSL context is used to build an HttpClient with a
     * [NoopHostnameVerifier]. Finally, the HttpClient is wrapped in a
     * [HttpComponentsClientHttpRequestFactory] and provided to the TestRestTemplate.
     *
     * @return a TestRestTemplate suitable for HTTPS integration tests with self-signed certificates
     */
    @Bean
    fun createTestRestTemplate(): TestRestTemplate {
        val trustAll =
            arrayOf<TrustManager>(
                object : X509TrustManager {
                    override fun getAcceptedIssuers(): Array<X509Certificate> = emptyArray()

                    override fun checkClientTrusted(
                        chain: Array<X509Certificate>,
                        authType: String,
                    ) {}

                    override fun checkServerTrusted(
                        chain: Array<X509Certificate>,
                        authType: String,
                    ) {}
                },
            )

        val sslCtx =
            SSLContext.getInstance("TLS").apply {
                init(null, trustAll, SecureRandom())
            }

        val sslFactory = SSLConnectionSocketFactory(sslCtx, NoopHostnameVerifier.INSTANCE)

        val registry =
            RegistryBuilder
                .create<org.apache.hc.client5.http.socket.ConnectionSocketFactory>()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", sslFactory)
                .build()

        val connManager = PoolingHttpClientConnectionManager(registry)
        val client =
            HttpClients
                .custom()
                .setConnectionManager(connManager)
                .build()

        val requestFactory = HttpComponentsClientHttpRequestFactory(client)

        return TestRestTemplate(
            RestTemplateBuilder().requestFactory(Supplier { requestFactory }),
        )
    }
}
