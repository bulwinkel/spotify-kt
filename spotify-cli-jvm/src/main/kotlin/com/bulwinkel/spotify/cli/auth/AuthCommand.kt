package com.bulwinkel.spotify.cli.auth

import com.bulwinkel.spotify.cli.Command
import fire.log.Fire

object AuthCommand : Command {

    override val description: String =
            "Authenticates with spotify saving authentication details to your home directory"

    override fun action(args: Array<String>) {
        Fire.d { "AuthCommand args: ${args.toList()}" }


    }
}