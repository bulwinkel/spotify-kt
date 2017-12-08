package com.bulwinkel.spotify.api

import com.bulwinkel.spotify.api.models.*
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.*

internal const val SPOTIFY_API_BASE_URL = "https://api.spotify.com/v1/"

internal interface ApiService {

    @GET("me/tracks")
    fun savedTracks(
            @Header("Authorization") authHeader: String,
            @Query("limit") limit: Int? = null,
            @Query("offset") offset: Int? = null,
            @Query("market") market: String? = null
    ): Single<PagingObject<SavedTrack>>

    /**
     * [Official documentation](https://developer.spotify.com/web-api/search-item/)
     */
    @GET("search?type=artist")
    fun searchArtists(
            @Header("Authorization") authHeader: String,
            @Query("q") query: String,
            @Query("market") market: String? = null,
            @Query("limit") limit: Int? = null,
            @Query("offset") offset: Int? = null
    ): Single<ArtistsSearchResult>

    /**
     * Get a list of the playlists owned or followed by the current Spotify user.
     *
     * [Official documentation](https://developer.spotify.com/web-api/get-a-list-of-current-users-playlists/)
     */
    @GET("me/playlists")
    fun playlists(
            @Header("Authorization") authHeader: String,
            @Query("limit") limit: Int? = null,
            @Query("offset") offset: Int? = null
    ): Single<PagingObject<PlaylistSimplified>>

    @GET("users/{user_id}/playlists")
    fun playlists(
            @Header("Authorization") authHeader: String,
            @Path("user_id") userId: String,
            @Query("limit") limit: Int? = null,
            @Query("offset") offset: Int? = null
    ) : Single<PagingObject<PlaylistSimplified>>
}