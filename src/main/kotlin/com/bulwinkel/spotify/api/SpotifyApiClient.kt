package com.bulwinkel.spotify.api

import com.bulwinkel.spotify.api.internal.Base64
import com.bulwinkel.spotify.api.models.PagingObject
import com.bulwinkel.spotify.api.models.SavedTrack
import com.bulwinkel.spotify.auth.AuthService
import com.bulwinkel.spotify.auth.SPOTIFY_AUTH_BASE_URL
import com.squareup.moshi.Moshi
import fire.log.Fire
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

class SpotifyApiClient(
    private val clientId: String,
    private val clientSecret: String,
    private val redirectUri: String,
    private val authorizationCode: String,
    private val credentialStore: CredentialStore = InMemoryCredentialsStore()
) {

  private val moshi:Moshi = Moshi.Builder().build()
  private val httpClient:OkHttpClient = OkHttpClient().newBuilder()
      .addInterceptor(HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
        Fire.d("OkHttp") { it }
      }).setLevel(BODY))
      .build()
  private val retrofitBuilder = Retrofit.Builder()
      .client(httpClient)
      .addConverterFactory(MoshiConverterFactory.create(moshi))
      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
  private val apiService: ApiService = retrofitBuilder
      .baseUrl(SPOTIFY_API_BASE_URL)
      .build()
      .create(ApiService::class.java)
  private val authService: AuthService = retrofitBuilder
      .baseUrl(SPOTIFY_AUTH_BASE_URL)
      .build()
      .create(AuthService::class.java)

  private val basicAuthHeader : String =
      "Basic ${Base64.encodeToString("$clientId:$clientSecret".toByteArray(), Base64.NO_WRAP)}"

  // expiresIn is in seconds
  private fun calculatedExpiresAt(expiresIn: Int) : Long {
    return System.currentTimeMillis() + ((expiresIn - 5) * 1000)
  }

  fun token() : Single<SpotifyCredentialsCache> {
    return authService.token(
        authHeader = basicAuthHeader,
        redirectUri = redirectUri,
        code = authorizationCode,
        grantType = "authorization_code"
    ).flatMap { token ->
      val creds = SpotifyCredentialsCache(
          accessToken = token.access_token,
          tokenType = token.token_type,
          scope = token.scope,
          expiresAt = calculatedExpiresAt(token.expires_in),
          refreshToken = token.refresh_token
      )
      return@flatMap credentialStore.save(creds)
          .toSingleDefault(creds)
    }
  }

  /**
   * Single containing a valid auth header, checks if the token stored in the [CredentialStore]
   * has expired and if so refreshes and store it before returning the refreshed value.
   */
  private val authHeader: Single<String> = credentialStore.load()
      .flatMap { creds ->
        if (creds.expiresAt <= System.currentTimeMillis()) {
          authService.refreshToken(basicAuthHeader, creds.refreshToken)
              .flatMap { refreshedToken ->
                val updatedCredentials = creds.copy(
                    accessToken = refreshedToken.access_token,
                    tokenType = refreshedToken.token_type,
                    scope = refreshedToken.scope,
                    expiresAt = calculatedExpiresAt(refreshedToken.expires_in)
                )
                credentialStore.save(updatedCredentials)
                    .toSingleDefault(updatedCredentials)
              }
        } else {
          Single.just(creds)
        }
      }
      .map { "${it.tokenType} ${it.accessToken}" }

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
  ) : Single<PagingObject<SavedTrack>> {
    return authHeader
        .flatMap { authHeader ->
          apiService.savedTracks(
              authHeader = authHeader,
              limit = limit,
              offset = offset,
              market = market)
        }
  }
}