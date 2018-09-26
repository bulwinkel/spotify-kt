package com.bulwinkel.spotify.api

import com.bulwinkel.spotify.api.models.ArtistsSearchResult
import com.bulwinkel.spotify.api.models.PagingObject
import com.bulwinkel.spotify.api.models.PlaylistSimplified
import com.bulwinkel.spotify.api.models.SavedTrack
import com.squareup.moshi.Moshi
import io.reactivex.Single
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

class SpotifyApiClient(
        private val tokenSource: Single<String>
) {

    private val moshi: Moshi = Moshi.Builder().build()
    private val httpClient: OkHttpClient = OkHttpClient().newBuilder()
//            .addInterceptor(HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
//                Fire.d("OkHttp") { it }
//            }).setLevel(BODY))
            .build()
    private val retrofitBuilder = Retrofit.Builder()
            .client(httpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    private val apiService: ApiService = retrofitBuilder
            .baseUrl(SPOTIFY_API_BASE_URL)
            .build()
            .create(ApiService::class.java)

    private val authHeader: Single<String> get() = tokenSource.map { "Bearer $it" }

    /**
     * Get a list of the songs saved in the current Spotify user’s “Your Music” library.
     *
     * The `user-library-read` scope must have been authorized by the user.
     *
     * @param limit Optional. The maximum number of objects to return.
     *              Default: 20. Minimum: 1. Maximum: 50.
     * @param offset Optional. The index of the first object to return. Default: 0
     *               (i.e., the first object). Use with limit to get the next set of objects.
     * @param market Optional. An ISO 3166-1 alpha-2 country code. Provide this parameter if you
     *               want to apply Track Relinking.
     */
    fun savedTracks(
            limit: Int? = null,
            offset: Int? = null,
            market: String? = null
    ): Single<PagingObject<SavedTrack>> {
        return authHeader
                .flatMap { authHeader ->
                    apiService.savedTracks(
                            authHeader = authHeader,
                            limit = limit,
                            offset = offset,
                            market = market)
                }
    }

    fun searchArtists(
            query: String,
            market: String? = null,
            limit: Int? = null,
            offset: Int? = null
    ): Single<ArtistsSearchResult> = authHeader.flatMap {
        apiService.searchArtists(
                authHeader = it,
                query = query,
                market = market,
                limit = limit,
                offset = offset
        )
    }

    /**
     * Get a list of the playlists owned or followed by a Spotify user.
     */
    fun playlists(
            userId: String? = null,
            limit: Int? = null,
            offset: Int? = null
    ): Single<PagingObject<PlaylistSimplified>> = authHeader.flatMap { authHeader ->
        if (userId == null || userId.isBlank()) {
            apiService.playlists(
                    authHeader = authHeader,
                    limit = limit,
                    offset = offset
            )
        } else {
            apiService.playlists(
                    authHeader = authHeader,
                    userId = userId,
                    limit = limit,
                    offset = offset
            )
        }
    }
}