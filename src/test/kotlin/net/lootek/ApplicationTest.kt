package net.lootek

import io.ktor.server.testing.*
import net.lootek.plugins.configureRouting
import kotlin.test.Test


class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        application {
            configureRouting()
        }
//        client.get("/").apply {
//            assertEquals(HttpStatusCode.OK, status)
//            assertEquals("Hello World!", bodyAsText())
//        }
    }
}