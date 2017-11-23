package com.bulwinkel.spotify.api

import com.bulwinkel.spotify.api.models.PagingObject
import com.bulwinkel.spotify.api.models.RefreshedToken
import com.bulwinkel.spotify.api.models.SavedTrack
import com.bulwinkel.spotify.api.models.Token
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

internal const val SPOTIFY_API_BASE_URL = "https://api.spotify.com/v1/"

internal interface ApiService {

  @GET("me/tracks") fun savedTracks(
      @Header("Authorization") authHeader: String,
      @Query("limit") limit: Int? = null,
      @Query("offset") offset: Int? = null,
      @Query("market") market: String? = null
  ) : Single<PagingObject<SavedTrack>>
}