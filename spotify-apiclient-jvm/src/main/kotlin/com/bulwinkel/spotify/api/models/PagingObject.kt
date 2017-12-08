package com.bulwinkel.spotify.api.models

/**
 * The offset-based paging object is a container for a set of objects. It contains a key called
 * items (whose value is an array of the requested objects) along with other keys like previous,
 * next and limit that can be useful in future calls.
 *
 * [official docs](https://developer.spotify.com/web-api/object-model/#paging-object)
 */
data class PagingObject<out T>(
    /** A link to the Web API endpoint returning the full result of the request. */
    val href: String,
    /** The requested data. */
    val items: List<T>,
    /** The maximum number of items in the response (as set in the query or by default). */
    val limit: Int,
    /** URL to the next page of items. (null if none) */
    val next: String?,
    /** The offset of the items returned (as set in the query or by default). */
    val offset: Int,
    /** URL to the previous page of items. (null if none) */
    val previous: String,
    /** The total number of items available to return. */
    val total: Int
)