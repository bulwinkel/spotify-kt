package com.bulwinkel.spotify.api.models

data class SpotifyUser(
        /**
         * The user's date-of-birth.
         *
         * This field is only available when the current user has granted access to the
         * user-read-birthdate scope.
         */
        val birthdate: String? = null,
        /**
         * 	The country of the user, as set in the user's account profile.
         * 	An ISO 3166-1 alpha-2 country code.
         * 	This field is only available when the current user has granted access to the
         * 	user-read-private scope.
         */
        val country: String? = null,
        /**
         * 	The name displayed on the user's profile. null if not available.
         */
        val display_name: String? = null,
        /**
         * The user's email address, as entered by the user when creating their account.
         *
         * Important! This email address is unverified;
         * there is no proof that it actually belongs to the user.
         *
         * This field is only available when the current user has granted access to the
         * user-read-email scope.
         */
        val email: String? = null,
        /**
         * An external URL object	Known external URLs for this user.
         */
        val external_urls: Map<String, String>,
        /**
         * A followers object	Information about the followers of the user.
         */
        val followers: Followers,
        /**
         * A link to the Web API endpoint for this user.
         */
        val href: String,
        /**
         * The Spotify user ID for the user.
         */
        val id: String,
        /**
         * The user's profile image.
         */
        val images: List<Image>,
        /**
         * The user's Spotify subscription level: "premium", "free", etc.
         * (The subscription level "open" can be considered the same as "free".)
         *
         * This field is only available when the current user has granted access to the
         * user-read-private scope.
         */
        val product: String,
        /**
         * The object type: "user"
         */
        val type: String,
        /**
         * The Spotify URI for the user.
         */
        val uri: String
)