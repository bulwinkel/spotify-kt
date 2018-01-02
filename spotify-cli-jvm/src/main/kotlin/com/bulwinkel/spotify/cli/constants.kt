package com.bulwinkel.spotify.cli

import java.io.File

val userHomeDir = File(System.getProperty("user.home"))

operator fun File.plus(child: String) : File = File(this, child)

val cliCacheDirName = ".spotify-cli"

val cliCacheDir by lazy {
    val dir = userHomeDir + cliCacheDirName
    if (!dir.exists()) dir.mkdirs()
    dir
}
val authCacheFile = cliCacheDir + "authCache.json"