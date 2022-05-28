package net.lootek.mpd

import org.bff.javampd.server.MPD

class MPD {
    companion object {
        val Builder: MPD.Builder
            get() {
                return MPD.Builder().server("192.168.10.18")
            }
    }
}

fun MPD.addAndPlay(file: String) {
    this.playlist.addSong(file, true)
}

fun MPD.status(): String {
    val status = this.serverStatus
    return "${status.state} ${status.volume}"
}

fun MPD.statistics(): String {
    val stats = this.serverStatistics
    return "${stats.uptime}"
}