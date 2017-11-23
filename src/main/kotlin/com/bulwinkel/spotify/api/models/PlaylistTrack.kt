package com.bulwinkel.spotify.api.models

data class PlaylistTrack(
        /**
         * The date and time the track was added.
         *
         * Note that some very old playlists may return null in this field.
         */
        val added_at: String?,
        /**
         * The Spotify user who added the track.
         *
         * Note that some very old playlists may return null in this field.
         */
        val added_by: SpotifyUser?,
        /**
         * Whether this track is a local file or not.
         */
        val is_local: Boolean,
        /**
         * Information about the track.
         */
        val track: TrackFull
)