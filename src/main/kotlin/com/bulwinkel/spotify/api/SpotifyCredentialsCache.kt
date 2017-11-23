package com.bulwinkel.spotify.api

data class SpotifyCredentialsCache(
  /**
   * An access token that can be provided in subsequent calls,
   * for example to Spotify Web API services.
   */
  val accessToken: String,
  /** How the access token may be used: always "Bearer". */
  val tokenType: String,
  /** A space-separated list of scopes which have been granted for this access_token */
  val scope: String,
  /**	The time (in millis) at which the access token expires. */
  val expiresAt: Long,
  /**
   * A token that can be sent to the Spotify Accounts service in place of an authorization code.
   * (When the access code expires, send a POST request to the Accounts service /api/token
   * endpoint, but use this code in place of an authorization code. A new access token will be
   * returned. A new refresh token might be returned too.)
   */
  val refreshToken: String
)