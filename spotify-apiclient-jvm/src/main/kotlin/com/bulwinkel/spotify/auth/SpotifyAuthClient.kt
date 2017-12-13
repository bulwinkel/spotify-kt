package com.bulwinkel.spotify.auth

import com.bulwinkel.internal.Base64
import com.bulwinkel.spotify.api.models.RefreshedToken
import com.bulwinkel.spotify.auth.models.SpotifyApp
import com.squareup.moshi.Moshi
import fire.log.Fire
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Field
import retrofit2.http.Header

class SpotifyAuthClient(
        private val app: SpotifyApp
) {
    private val moshi: Moshi = Moshi.Builder().build()
    private val httpClient: OkHttpClient = OkHttpClient().newBuilder()
            .addInterceptor(HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
                Fire.d("OkHttp") { it }
            }).setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    private val retrofitBuilder = Retrofit.Builder()
            .client(httpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    private val authService: AuthService = retrofitBuilder
            .baseUrl(SPOTIFY_AUTH_BASE_URL)
            .build()
            .create(AuthService::class.java)

    private val basicAuthHeader : String =
            "Basic ${Base64.encodeToString("${app.clientId}:${app.clientSecret}".toByteArray(), Base64.NO_WRAP)}"

    fun refreshToken(refreshToken: String) : Single<RefreshedToken> = authService.refreshToken(
            authHeader = basicAuthHeader,
            refreshToken = refreshToken
    )

    fun token(code: String) = authService.token(
            authHeader = basicAuthHeader,
            redirectUri = app.redirectUri,
            code = code
    )
}