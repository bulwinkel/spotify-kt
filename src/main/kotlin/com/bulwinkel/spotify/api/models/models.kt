package com.bulwinkel.spotify.api.models

//region: Auth

data class Token(
    /**
     * An access token that can be provided in subsequent calls,
     * for example to Spotify Web API services.
     */
    val access_token: String,
    /** How the access token may be used: always "Bearer". */
    val token_type: String,
    /** A space-separated list of scopes which have been granted for this access_token */
    val scope: String,
    /**	The time period (in seconds) for which the access token is valid. */
    val expires_in: Int,
    /**
     * A token that can be sent to the Spotify Accounts service in place of an authorization code.
     * (When the access code expires, send a POST request to the Accounts service /api/token
     * endpoint, but use this code in place of an authorization code. A new access token will be
     * returned. A new refresh token might be returned too.)
     */
    val refresh_token: String
)

data class RefreshedToken(
    /**
     * An access token that can be provided in subsequent calls,
     * for example to Spotify Web API services.
     */
    val access_token: String,
    /** How the access token may be used: always "Bearer". */
    val token_type: String,
    /** A space-separated list of scopes which have been granted for this access_token */
    val scope: String,
    /**	The time period (in seconds) for which the access token is valid. */
    val expires_in: Int
)

//endregion

/**
 * [official docs](https://developer.spotify.com/web-api/object-model/#saved-track-object)
 */
data class SavedTrack(
    /**
     * The date and time the album was saved in ISO 8601 format as Coordinated Universal Time
     * (UTC) with zero offset: YYYY-MM-DDTHH:MM:SSZ
     */
    val added_at: String,
    /** Information about the track. */
    val track: TrackFull
)

/**
 * [official docs](https://developer.spotify.com/web-api/object-model/#track-object-full)
 */
data class TrackFull(
    /**
     * The album on which the track appears.
     * The album object includes a link in href to full information about the album.
     */
    val album: AlbumSimplified,
    /**
     * The artists who performed the track. Each artist object includes a link in href
     * to more detailed information about the artist.
     */
    val artists: List<ArtistSimplified>,
    /**
     * A list of the countries in which the track can be played, identified by their
     * [ISO 3166-1 alpha-2](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2) code.
     */
    val available_markets: List<String>,
    /**
     * The disc number (usually 1 unless the album consists of more than one disc).
     */
    val disc_number: Int,
    /**
     * The track length in milliseconds.
     */
    val duration_ms: Int,
    /**
     * Whether or not the track has explicit lyrics
     * (true = yes it does; false = no it does not OR unknown).
     */
    val explicit: Boolean,
    /**
     * Known external IDs for the track.
     */
    val external_ids: Map<String, String>,
    /**
     * Known external URLs for this track.
     */
    val external_urls: Map<String, String>,
    /**
     * A link to the Web API endpoint providing full details of the track.
     */
    val href:	String,
    /**
     * The Spotify ID for the track.
     */
    val id: String,
    /**
     * Part of the response when
     * [Track Relinking](https://developer.spotify.com/web-api/track-relinking-guide/) is applied.
     * If true, the track is playable in the given market. Otherwise false.
     */
    val is_playable: Boolean,
    /**
     * Part of the response when
     * [Track Relinking](https://developer.spotify.com/web-api/track-relinking-guide/) is applied,
     * and the requested track has been replaced with different track. The track in the
     * linked_from object contains information about the originally requested track.
     */
    val linked_from: TrackLink,
    /**
     * The name of the track.
     */
    val name: String,
    /**
     * The popularity of the track. The value will be between 0 and 100,
     * with 100 being the most popular.
     *
     * The popularity of a track is a value between 0 and 100, with 100 being the most popular.
     * The popularity is calculated by algorithm and is based, in the most part, on the total
     * number of plays the track has had and how recent those plays are.
     *
     * Generally speaking, songs that are being played a lot now will have a higher popularity
     * than songs that were played a lot in the past. Duplicate tracks (e.g. the same track
     * from a single and an album) are rated independently. Artist and album popularity is
     * derived mathematically from track popularity. Note that the popularity value may lag
     * actual popularity by a few days: the value is not updated in real time.
     */
    val popularity: Int,
    /**
     * A link to a 30 second preview (MP3 format) of the track. null if not available.
     */
    val preview_url: String?,
    /**
     * The number of the track. If an album has several discs, the track number is the
     * number on the specified disc.
     */
    val track_number: Int,
    /**
     * The object type: "track"
     */
    val type: String,
    /**
     * The [Spotify URI](https://developer.spotify.com/web-api/user-guide/#spotify-uris-and-ids)
     * for the track.
     */
    val uri: String
)

/**
 * [official docs](https://developer.spotify.com/web-api/object-model/#track-link)
 */
data class TrackLink(
    /**
     * Known external URLs for this track.
     */
    val external_urls: Map<String, String>,
    /**
     * A link to the Web API endpoint providing full details of the track.
     */
    val href:	String,
    /**
     * The Spotify ID for the track.
     */
    val id: String,
    /**
     * The object type: "track"
     */
    val type: String,
    /**
     * The [Spotify URI](https://developer.spotify.com/web-api/user-guide/#spotify-uris-and-ids)
     * for the track.
     */
    val uri: String
)

/**
 * [official docs](https://developer.spotify.com/web-api/object-model/#album-object-simplified)
 */
data class AlbumSimplified(
    //// TODO: 24/9/17 change to an enum
    /** The type of the album: one of "album", "single", or "compilation". */
    val album_type: String,
    /**
     * The artists of the album. Each artist object includes a link in href to
     * more detailed information about the artist.
     */
    val artists: List<ArtistSimplified>,
    /**
     * The markets in which the album is available: ISO 3166-1 alpha-2 country codes.
     * Note that an album is considered available in a market when at least 1 of its
     * tracks is available in that market.
     */
    val available_markets: List<String>,
    /** Known external URLs for this album. */
    val external_urls: Map<String, String>,
    /**	A link to the Web API endpoint providing full details of the album. */
    val href: String,
    /** The Spotify ID for the album. */
    val id: String,
    /** The cover art for the album in various sizes, widest first. */
    val images: List<Image>,
    /** The name of the album. In case of an album takedown, the value may be an empty string. */
    val name: String,
    /** The object type: "album" */
    val type: String,
    /**
     * The [Spotify URI](https://developer.spotify.com/web-api/user-guide/#spotify-uris-and-ids)
     * for the album.
     */
    val uri: String
)

/**
 * [official docs](https://developer.spotify.com/web-api/object-model/#image-object)
 */
data class Image(
    /** The image height in pixels. If unknown: null or not returned. */
    val height: Int?,
    /** The image width in pixels. If unknown: null or not returned. */
    val width: Int?,
    /** The source URL of the image. */
    val url: String
)

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