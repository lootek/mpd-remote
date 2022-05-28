package net.lootek.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.locations.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.webjars.*
import net.lootek.mpd.MPDController
import net.lootek.youtube.YouTube

fun Application.configureRouting() {
    install(AutoHeadResponse)
    install(Locations) {
    }

    install(Webjars) {
        path = "/webjars" //defaults to /webjars
    }

    val mpd = MPDController()
    val youtube = YouTube()

    routing {
        get("/stats") {
            call.respondText(mpd.statistics(), ContentType.Text.Html)
        }
        get("/status") {
            call.respondText(mpd.status(), ContentType.Text.Html)
        }
        get("/videos") {
            call.respondText(youtube.getFirstVideoFromPlaylist("PLFn1VIsptN2J4c_yBrL-tFZ62maPvcv9J"))
        }

        get<MyLocation> {
            call.respondText("Location: name=${it.name}, arg1=${it.arg1}, arg2=${it.arg2}")
        }
        // Register nested routes
        get<Type.Edit> {
            call.respondText("Inside $it")
        }
        get<Type.List> {
            call.respondText("Inside $it")
        }
        // Static plugin. Try to access `/static/index.html`
        static("/static") {
            resources("static")
        }
        get("/webjars") {
            call.respondText("<script src='/webjars/jquery/jquery.js'></script>", ContentType.Text.Html)
        }
    }
}

@Location("/location/{name}")
class MyLocation(val name: String, val arg1: Int = 42, val arg2: String = "default")

@Location("/type/{name}")
data class Type(val name: String) {
    @Location("/edit")
    data class Edit(val type: Type)

    @Location("/list/{page}")
    data class List(val type: Type, val page: Int)
}
