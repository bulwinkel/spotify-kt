package com.bulwinkel.spotify.api.models

data class ArtistsSearchResult(
    val artists: PagingObject<ArtistFull>
)