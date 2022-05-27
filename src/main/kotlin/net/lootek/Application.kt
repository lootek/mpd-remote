package net.lootek

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import net.lootek.plugins.configureRouting
import net.lootek.plugins.configureTemplating

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureRouting()
//        configureSecurity()
        configureTemplating()
    }.start(wait = true)
}
