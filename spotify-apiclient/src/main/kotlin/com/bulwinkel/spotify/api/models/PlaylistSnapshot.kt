package com.bulwinkel.spotify.api.models

data class PlaylistSnapshot(
        /**
         * e.g.
         * { "snapshot_id" : "JbtmHBDBAYu3/bt8BOXKjzKx3i0b6LCa/wVjyl6qQ2Yf6nFXkbmzuEa+ZI/U1yF+" }
         */
        val snapshot_id: String
)