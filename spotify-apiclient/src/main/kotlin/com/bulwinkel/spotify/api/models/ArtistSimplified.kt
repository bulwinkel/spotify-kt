package com.bulwinkel.spotify.api.models

/**
 * [official docs](https://developer.spotify.com/web-api/object-model/#artist-object-simplified)
 */
data class ArtistSimplified(
    /** Known external URLs for this artist. */
    val external_urls: Map<String, String>,
    /** A link to the Web API endpoint providing full details of the artist. */
    val href: String,
    /** The Spotify ID for the artist. */
    val id: String,
    /** The name of the artist */
    val name: String,
    /** The object type: "artist" */
    val type: String,
    /**
     * The [Spotify URI](https://developer.spotify.com/web-api/user-guide/#spotify-uris-and-ids)
     * for the artist.
     */
    val uri: String
)