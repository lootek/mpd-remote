package net.lootek.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.css.*
import kotlinx.html.*
import net.lootek.youtube.*

fun Application.configureTemplating() {
    data class Channel(val id: String, val title: String)
    data class Video(val id: String, val title: String)
    data class Playlist(val id: String, val channel: Channel, val video: Video)

    val youtube = YouTube()
    val channels: MutableMap<String, MutableList<Playlist>> = mutableMapOf()
    for (playlistID in listOf(
        "PLFn1VIsptN2J4c_yBrL-tFZ62maPvcv9J",
        "PLFn1VIsptN2JqIV0kOafVnVrVdK9_dlR8",
        "PLFn1VIsptN2IJDBuNrbTH5wJgfmau1WQw",
        "PLXUja6BNv4Jw40HWItm8G3VV0DcgzEpti",
        "UUs7O9sOUQiBGBxaaAguIwig",

        )) {
        val playlists = channels[youtube.getPlaylist(playlistID).channelID()] ?: mutableListOf()
        playlists.add(
            Playlist(
                playlistID,
                Channel(
                    youtube.getPlaylist(playlistID).channelID(),
                    youtube.getPlaylist(playlistID).channelTitle(),
                ),
                Video(
                    youtube.getPlaylist(playlistID).firstVideo().id(),
                    youtube.getPlaylist(playlistID).firstVideo().title(),
                ),
            )
        )
        channels[youtube.getPlaylist(playlistID).channelID()] = playlists
    }

    routing {
        get("/") {
            call.respondHtml {
                head {
                    link(rel = "stylesheet", href = "/styles.css", type = "text/css")
                    title("mpd remote")
                }

                body {
                    ul {
                        for ((_, playlists) in channels) {
                            li {
                                span {
                                    +"${playlists[0].channel.title}"
                                }
                                br {}

                                ul {
                                    for (i in playlists.indices) {
                                        li {
                                            a {
                                                href = "youtube/${playlists[i].video.id}/play"
                                                +"${playlists[i].video.title}"
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        get("/styles.css") {
            call.respondCss {
                body {
                    backgroundColor = Color.black
                    color = Color.white
                    margin(20.px)
                }

                rule("a") {
                    color = Color.white
                }
            }
        }
    }
}

suspend inline fun ApplicationCall.respondCss(builder: CSSBuilder.() -> Unit) {
    this.respondText(CSSBuilder().apply(builder).toString(), ContentType.Text.CSS)
}

