package com.bulwinkel.spotify.cli

interface Command {
    val description: String
    fun action(args: Array<String>): Unit
}