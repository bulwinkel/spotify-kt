package com.bulwinkel.spotify.cli

import com.bulwinkel.spotify.cli.auth.AuthCommand
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import kotlinx.serialization.Optional
import kotlinx.serialization.Serializable
import kotlinx.serialization.internal.ArrayListSerializer
import kotlin.system.exitProcess

@Serializable
data class GistCommit(
        @Optional val committed_at: String = "",
        @Optional val url: String = "",
        @Optional val version: String = ""
)

suspend fun main(args: Array<String>) {
//    Fire.add(printLnLog)
//    Fire.d { "args = ${args.toList()}" }
//    try {
//        val commandName = args.first()
//        Commands.valueOf(commandName).command.action(args.sliceArray(1 until args.size))
//    } catch (e: Exception) {
//        Fire.e(t = e) { "e.message = ${e.message}" }
//        Commands.describe()
//    }

    val httpClient = HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer().apply {
                setMapper(GistCommit::class, GistCommit.serializer())
            }
        }
    }

    val commits = httpClient.get<List<GistCommit>>("https://api.github.com/gists/b1c371d447df071b90fe6e331d8b7d98/commits")
    println("commits = $commits")
    exitProcess(0)
}

@Suppress("EnumEntryName")
enum class Commands(val command: Command) {
    auth(AuthCommand);

    companion object {
        fun describe() {
            val commandsHelp = Commands.values().joinToString("\n",
                    transform = { "  - ${it.name}\n    ${it.command.description}" })
            println("Available commands:\n$commandsHelp ")
        }
    }
}