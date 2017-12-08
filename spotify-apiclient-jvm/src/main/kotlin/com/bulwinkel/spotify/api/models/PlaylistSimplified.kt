package com.bulwinkel.spotify.api.models

data class PlaylistSimplified(
        /**
         * true if the owner allows other users to modify the playlist.
         */
        val collaborative: Boolean,
        /**
         * Known external URLs for this playlist.
         */
        val external_urls: Map<String, String>,
        /**
         * A link to the Web API endpoint providing full details of the playlist.
         */
        val href: String,
        /**
         * The Spotify ID for the playlist.
         */
        val id: String,
        /**
         * Images for the playlist. The array may be empty or contain up to three images. The images
         * are returned by size in descending order. See Working with Playlists.
         *
         * Note: If returned, the source URL for the image (url) is temporary and will
         * expire in less than a day.
         */
        val images: List<Image>,
        /**
         * The name of the playlist.
         */
        val name: String,
        /**
         * The user who owns the playlist
         */
        val owner: SpotifyUser,
        /**
         * The playlist's public/private status: true the playlist is public,
         * false the playlist is private, null the playlist status is not relevant.
         *
         * For more about public/private status, see Working with Playlists.
         */
        val public: Boolean?,
        /**
         * 	The version identifier for the current playlist.
         *
         * 	Can be supplied in other requests to target a specific playlist version:
         * 	see Remove tracks from a playlist
         */
        val snapshot_id: String,
        /**
         * Information about the tracks of the playlist.
         */
        val tracks: PagingObject<PlaylistTrack>,
        /**
         * The object type: "playlist"
         */
        val type: String,
        /**
         * 	The Spotify URI for the playlist.
         */
        val uri: String
)