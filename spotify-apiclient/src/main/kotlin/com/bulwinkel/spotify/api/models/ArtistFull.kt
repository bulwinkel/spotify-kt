package com.bulwinkel.spotify.api.models

/**
 * [official docs](https://developer.spotify.com/web-api/object-model/#artist-object-full)
 */
data class ArtistFull(
        /**
         * Known external URLs for this artist.
         */
        val external_urls: Map<String, String>,
        /**
         * Information about the followers of the artist.
         */
        val followers: Followers,
        /**
         * A list of the genres the artist is associated with. For example: "Prog Rock",
         * "Post-Grunge". (If not yet classified, the array is empty.)
         */
        val genres: List<String>,
        /**
         * A link to the Web API endpoint providing full details of the artist.
         */
        val href: String,
        /**
         * The Spotify ID for the artist.
         */
        val id: String,
        /**
         * Images of the artist in various sizes, widest first.
         */
        val images: List<Image>,
        /**
         * The name of the artist
         */
        val name: String,
        /**
         * The popularity of the artist. The value will be between 0 and 100, with 100 being the
         * most popular. The artist's popularity is calculated from the popularity of all the
         * artist's tracks.
         */
        val popularity: Int,
        /**
         * The object type: "artist"
         */
        val type: String,
        /**
         * The [Spotify URI](https://developer.spotify.com/web-api/user-guide/#spotify-uris-and-ids)
         * for the artist.
         */
        val uri: String
)