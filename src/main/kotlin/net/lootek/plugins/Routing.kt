package net.lootek.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.locations.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.webjars.*
import net.lootek.mpd.MPD
import net.lootek.mpd.addAndPlay
import net.lootek.mpd.statistics
import net.lootek.mpd.status
import net.lootek.youtube.YouTube
import net.lootek.youtube.first
import net.lootek.youtube.id
import net.lootek.youtube.ytdlURL

fun Application.configureRouting() {
    install(AutoHeadResponse)
    install(Locations) {
    }

    install(Webjars) {
        path = "/webjars" //defaults to /webjars
    }

    val mpd = MPD.Builder.build()
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
                "url" -> call.respondText(ytdlURL(youtube.getVideoFromPlaylist(it.id).first().id()))
                "details" -> call.respondText(youtube.getVideoFromPlaylist(it.id).first().toPrettyString())
                "play" -> mpd.addAndPlay(ytdlURL(youtube.getVideoFromPlaylist(it.id).first().id()))
            }
        }

        static("/") {
            staticBasePackage = "static"
            resource("index.html")
            defaultResource("index.html")
            static("images") {
                resource("ktor_logo.png")
                resource("image.png", "ktor_logo.png")
            }
            static("assets") {
                resources("css")
                resources("js")
            }
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
