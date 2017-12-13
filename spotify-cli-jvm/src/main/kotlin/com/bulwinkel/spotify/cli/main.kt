package com.bulwinkel.spotify.cli

import com.bulwinkel.spotify.cli.auth.AuthCommand
import fire.log.Fire
import fire.log.printLnLog

fun main(args: Array<String>) {
    Fire.add(printLnLog)
    Fire.d { "args = ${args.toList()}" }
    try {
        val commandName = args.first()
        Commands.valueOf(commandName).command.action(args.sliceArray(1 until args.size))
    } catch (e: Exception) {
        Fire.e(t = e) { "e.message = ${e.message}" }
        Commands.describe()
    }
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