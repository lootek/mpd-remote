package net.lootek.youtube

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.HttpRequest
import com.google.api.client.http.HttpRequestInitializer
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.youtube.YouTube
import java.io.IOException


class YouTube {
    private var API_KEY = System.getenv("YOUTUBE_API_KEY") ?: "<YOUTUBE_API_KEY>"
    private val JSON_FACTORY: JsonFactory = GsonFactory.getDefaultInstance()

    companion object

    val Builder: YouTube.Builder
        get() {
            val httpTransport: NetHttpTransport = GoogleNetHttpTransport.newTrustedTransport()
            return YouTube.Builder(httpTransport, JSON_FACTORY, HttpRequestInitializer() {
                @Throws(IOException::class)
                fun initialize(req: HttpRequest) {
                    req.headers.set("Authorization", "Bearer $API_KEY")
                }
            })
        }
}