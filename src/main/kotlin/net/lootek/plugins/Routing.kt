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
import net.lootek.youtube.first
import net.lootek.youtube.url

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
        get<MPDLocation> {
            when (it.choice) {
                "stats" -> call.respondText(mpd.statistics(), ContentType.Text.Html)
                "status" -> call.respondText(mpd.status(), ContentType.Text.Html)
            }

        }

        get<YouTubeLocation> {
            when (it.choice) {
                "url" -> call.respondText(youtube.getVideoFromPlaylist(it.id).first().url())
                "details" -> call.respondText(youtube.getVideoFromPlaylist(it.id).first().toPrettyString())
            }
        }

//        // Register nested routes
//        get<Type.Edit> {
//            call.respondText("Inside $it")
//        }
//        get<Type.List> {
//            call.respondText("Inside $it")
//        }

        // Static plugin. Try to access `/static/index.html`
        static("/static") {
            resources("static")
        }
        get("/webjars") {
            call.respondText("<script src='/webjars/jquery/jquery.js'></script>", ContentType.Text.Html)
        }
    }
}

@Location("/youtube/{id}/{choice}")
class YouTubeLocation(val id: String, val choice: String = "details")

@Location("/mpd/{choice}")
class MPDLocation(val choice: String = "status")

//@Location("/type/{name}")
//data class Type(val name: String) {
//    @Location("/edit")
//    data class Edit(val type: Type)
//
//    @Location("/list/{page}")
//    data class List(val type: Type, val page: Int)
//}
