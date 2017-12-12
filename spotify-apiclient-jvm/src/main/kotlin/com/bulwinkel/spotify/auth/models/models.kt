package com.bulwinkel.spotify.auth.models

data class SpotifyApp(
        val clientId: String,
        val clientSecret: String,
        val redirectUri: String
)