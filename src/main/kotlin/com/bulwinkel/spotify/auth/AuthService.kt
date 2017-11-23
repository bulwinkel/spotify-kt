package com.bulwinkel.spotify.auth

import com.bulwinkel.spotify.api.models.RefreshedToken
import com.bulwinkel.spotify.api.models.Token
import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

internal const val SPOTIFY_AUTH_BASE_URL = "https://accounts.spotify.com/api/"

internal interface AuthService {

  @FormUrlEncoded
  @POST("token") fun token(
      @Header("Authorization") authHeader: String,
      @Field("redirect_uri") redirectUri: String,
      @Field("code") code: String,
      @Field("grant_type") grantType:String = "authorization_code"
  ) : Single<Token>

  /**
   * Access tokens are deliberately set to expire after a short time, after which new tokens
   * may be granted by supplying the refresh token originally obtained during the authorization
   * code exchange.
   *
   * @param authHeader   Base 64 encoded string that contains the client ID and client secret key.
   *                     The field must have the format:
   *                     Authorization: Basic <base64 encoded client_id:client_secret>
   * @param refreshToken the refresh token originally obtained during the authorization
   *                     code exchange
   * @param grantType    must be set to “refresh_token”
   * @return
   */
  @FormUrlEncoded
  @POST("token") fun refreshToken(
      @Header("Authorization") authHeader: String,
      @Field("refresh_token") refreshToken: String,
      @Field("grant_type") grantType: String = "refresh_token"
  ) : Single<RefreshedToken>
}