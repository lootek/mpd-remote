package net.lootek.youtube

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.HttpRequestInitializer
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.youtube.YouTube


class YouTube {
    private var API_KEY = System.getenv("YOUTUBE_API_KEY") ?: "<YOUTUBE_API_KEY>"
    private val JSON_FACTORY: JsonFactory = GsonFactory.getDefaultInstance()

    companion object

    val Builder: YouTube.Builder
        get() {
            val httpTransport: NetHttpTransport = GoogleNetHttpTransport.newTrustedTransport()
            return YouTube.Builder(httpTransport, JSON_FACTORY, HttpRequestInitializer() {})
        }

    fun getFirstVideoFromPlaylist(playlistID: String): String {
        val request = Builder.build()
            .playlistItems()
            .list(listOf("snippet", "contentDetails"))

        val response = request
            .setMaxResults(25L)
            .setPlaylistId(playlistID)
            .setKey(API_KEY)
//            .setRequestHeaders(com.google.api.client.http.HttpHeaders().setAuthorization("Bearer "))
            .execute()

        return response.toPrettyString()
    }
}