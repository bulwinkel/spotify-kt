package com.bulwinkel.spotify.api

import com.squareup.moshi.Moshi
import fire.log.Fire
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import java.io.File

interface CredentialStore {
  fun load() : Single<SpotifyCredentialsCache>
  fun save(credentialsCache: SpotifyCredentialsCache) : Completable
}

class InMemoryCredentialsStore : CredentialStore {

  private val subj = BehaviorSubject.create<SpotifyCredentialsCache>()

  override fun load(): Single<SpotifyCredentialsCache> {
    return subj.firstOrError()
  }

  override fun save(credentialsCache: SpotifyCredentialsCache): Completable {
    return Completable.fromAction { subj.onNext(credentialsCache) }
  }

}

class FileCredentialsStore(
    private val credentialsFile: File
) : CredentialStore {

  private val adapter = Moshi.Builder().build()
      .adapter(SpotifyCredentialsCache::class.java)
      .indent("  ")

  override fun load(): Single<SpotifyCredentialsCache> {
    return Single.fromCallable {
      val credentialsJson = credentialsFile.readText()
      //Fire.d { "credentialsJson = $credentialsJson" }
      return@fromCallable adapter.fromJson(credentialsJson)
    }
  }

  override fun save(credentialsCache: SpotifyCredentialsCache): Completable {
    return Completable.fromCallable {
      credentialsFile.writeText(adapter.toJson(credentialsCache))
    }
  }

}