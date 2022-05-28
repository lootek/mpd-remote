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
    val playlists: MutableList<Playlist> = mutableListOf()
    for (playlistID in listOf(
        "PLFn1VIsptN2J4c_yBrL-tFZ62maPvcv9J",
    )) {
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
            ),
        )
    }

    routing {
        get("/") {
            call.respondHtml {
                head {
                    link(rel = "stylesheet", href = "/styles.css", type = "text/css")
                    title = "mpd remote"
                }

                body {
                    ul {
                        for (p in playlists) {
                            li {
                                span {
                                    +"${p.channel.title}"
                                }
                                a {
                                    href = "youtube/${p.video.id}/play"
                                    +"${p.video.title}"
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

