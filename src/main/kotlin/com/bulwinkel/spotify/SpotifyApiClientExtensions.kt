package com.bulwinkel.spotify

import com.bulwinkel.spotify.api.SpotifyApiClient
import com.bulwinkel.spotify.api.models.PagingObject
import com.bulwinkel.spotify.api.models.SavedTrack
import io.reactivex.Observable

fun SpotifyApiClient.recursivelyFetchSavedTracks(
    limit: Int = 50,
    offset: Int = 0,
    market: String? = null,
    max: Int = 0 //unlimited
) : Observable<PagingObject<SavedTrack>> {
  return savedTracks(
      limit = limit,
      offset = offset,
      market = market
  ).toObservable()
      .concatMap { pagingObject ->
        val nextOffset = pagingObject.offset + pagingObject.limit
        if (pagingObject.next == null || (max in 1..nextOffset)) {
          Observable.just(pagingObject)
        } else {
          Observable.just(pagingObject)
              .concatWith(recursivelyFetchSavedTracks(max = max, offset = nextOffset))
        }
      }
}