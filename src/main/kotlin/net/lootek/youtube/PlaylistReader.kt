package net.lootek.youtube

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.HttpRequestInitializer
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.PlaylistItem
import com.google.api.services.youtube.model.PlaylistItemListResponse


class YouTube {
    private var apiKey = System.getenv("YOUTUBE_API_KEY") ?: "<YOUTUBE_API_KEY>"

    companion object {
        private val jsonFactory: JsonFactory = GsonFactory.getDefaultInstance()

        val Builder: YouTube.Builder
            get() {
                val httpTransport: NetHttpTransport = GoogleNetHttpTransport.newTrustedTransport()
                return YouTube.Builder(httpTransport, jsonFactory, HttpRequestInitializer() {})
            }
    }

    fun getVideoFromPlaylist(playlistID: String): PlaylistItemListResponse {
        val request = Builder
            .setApplicationName("mpd-remote")
            .build()
            .playlistItems()
            .list(listOf("snippet", "contentDetails"))

        return request
            .setMaxResults(5L)
            .setPlaylistId(playlistID)
            .setKey(apiKey)
            .execute()
    }
}

fun PlaylistItemListResponse.first(): PlaylistItem = this.items[0]
fun PlaylistItem.id(): String = this.snippet.resourceId.videoId