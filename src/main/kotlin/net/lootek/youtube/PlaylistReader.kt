package net.lootek.youtube

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.PlaylistItemListResponse
import java.io.IOException
import java.io.InputStreamReader
import java.security.GeneralSecurityException
import java.util.*


object ApiExample {
    private const val CLIENT_SECRETS = "client_secret.json"
    private val SCOPES: Collection<String> = Arrays.asList("https://www.googleapis.com/auth/youtube.readonly")
    private const val APPLICATION_NAME = "API code samples"
    private val JSON_FACTORY: JsonFactory =
        org.gradle.internal.impldep.com.google.api.client.json.jackson2.JacksonFactory.getDefaultInstance()

    /**
     * Create an authorized Credential object.
     *
     * @return an authorized Credential object.
     * @throws IOException
     */
    @Throws(IOException::class)
    fun authorize(httpTransport: org.gradle.internal.impldep.com.google.api.client.http.javanet.NetHttpTransport?): Credential {
        // Load client secrets.
        val `in` = ApiExample::class.java.getResourceAsStream(CLIENT_SECRETS)
        val clientSecrets: org.gradle.internal.impldep.com.google.api.client.googleapis.auth..oauth2.GoogleClientSecrets =
            org.gradle.internal.impldep.com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets.load(
                JSON_FACTORY,
                InputStreamReader(`in`)
            )
        // Build flow and trigger user authorization request.
        val flow: org.gradle.internal.impldep.com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow =
            org.gradle.internal.impldep.com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow.Builder(
                httpTransport,
                JSON_FACTORY,
                clientSecrets,
                SCOPES
            )
                .build()
        return AuthorizationCodeInstalledApp(flow, LocalServerReceiver()).authorize("user")
    }

    /**
     * Build and return an authorized API client service.
     *
     * @return an authorized API client service
     * @throws GeneralSecurityException, IOException
     */
    @get:Throws(GeneralSecurityException::class, IOException::class)
    val service: YouTube
        get() {
            val httpTransport: org.gradle.internal.impldep.com.google.api.client.http.javanet.NetHttpTransport =
                org.gradle.internal.impldep.com.google.api.client.googleapis.javanet.GoogleNetHttpTransport.newTrustedTransport()
            val credential: Credential = authorize(httpTransport)
            return Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build()
        }

    /**
     * Call function to create API service object. Define and
     * execute API request. Print API response.
     *
     * @throws GeneralSecurityException, IOException, GoogleJsonResponseException
     */
    @Throws(
        GeneralSecurityException::class,
        IOException::class,
        org.gradle.internal.impldep.com.google.api.client.googleapis.json.GoogleJsonResponseException::class
    )
    @JvmStatic
    fun main(args: Array<String>) {
        val youtubeService: YouTube = service
        // Define and execute the API request
        val request: YouTube.PlaylistItems.List = youtubeService.playlistItems()
            .list("snippet,contentDetails")
        val response: PlaylistItemListResponse = request.setMaxResults(25L)
            .setPlaylistId("PLFn1VIsptN2J4c_yBrL-tFZ62maPvcv9J")
            .execute()
        System.out.println(response)
    }
}