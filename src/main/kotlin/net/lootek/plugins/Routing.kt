package net.lootek.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.locations.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.webjars.*
import net.lootek.mpd.MPD
import net.lootek.mpd.statistics
import net.lootek.mpd.status
import net.lootek.youtube.*

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
                // id: playlistID
                "url" -> call.respondText(ytdlURL(youtube.getVideoFromPlaylist(it.id).first().id()))
                "title" -> call.respondText(ytdlURL(youtube.getVideoFromPlaylist(it.id).first().title()))
                "details" -> call.respondText(youtube.getVideoFromPlaylist(it.id).first().toPrettyString())

                // id: videoID
//                "play" -> mpd.addAndPlay(ytdlURL(it.id))
                "play" -> call.respondText(ytdlURL(it.id))
            }
        }
    }
}

@Location("/youtube/{id}/{choice}")
class YouTubeLocation(val id: String, val choice: String = "details")

@Location("/mpd/{choice}")
class MPDLocation(val choice: String = "status")
