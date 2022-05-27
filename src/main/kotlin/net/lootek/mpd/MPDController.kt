package net.lootek.mpd

import org.bff.javampd.server.MPD

class MPDController {
    companion object {
        val mpd: MPD = MPD.Builder().server("192.168.10.18").build()
    }

    fun status(): String {
        val status = mpd.serverStatus
        return "${status.state} ${status.volume}"
    }

    fun statistics(): String {
        val stats = mpd.serverStatistics
        return "${stats.uptime}"
    }
}