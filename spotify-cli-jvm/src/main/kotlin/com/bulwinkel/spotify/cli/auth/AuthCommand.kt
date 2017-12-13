package com.bulwinkel.spotify.cli.auth

import com.bulwinkel.spotify.cli.Command
import fire.log.Fire
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

object AuthCommand : Command {

    override val description: String =
            "Authenticates with spotify saving authentication details to your home directory"

    override fun action(args: Array<String>) {
        Fire.d { "AuthCommand args: ${args.toList()}" }

        val parsedArgs = AuthCommandArgs(args)
        Fire.d { "parsedArgs.clientId = ${parsedArgs.clientId}" }
        Fire.d { "parsedArgs.clientSecret = ${parsedArgs.clientSecret}" }
    }
}

class AuthCommandArgs(args: Array<String>) {

    private fun readLineOrThrow(prompt: String) : (argName: String) -> String {
        return {
            println(prompt)
            readLine() ?: throw NullPointerException()
        }
    }

    val clientId by StringArg(args, readLineOrThrow("Enter Client ID:"))
    val clientSecret by StringArg(args, readLineOrThrow("Enter Client Secret:"))
    val redirectUri by StringArg(args, readLineOrThrow("Enter RedirectUri:"))

}



class StringArg(
        private val args: Array<String>,
        private val defaultValue: (argName: String) -> String = {
            throw NullPointerException("arg $it must be provided")
        }
) : ReadOnlyProperty<AuthCommandArgs, String> {

    private var value: String? = null

    override fun getValue(thisRef: AuthCommandArgs, property: KProperty<*>): String {
        if (value == null) {
            val propName = property.name
            Fire.d {  "looking up value for property.name = $propName" }
            value = args.findValueOrNull("--$propName") ?: defaultValue(propName)
        }

        return value!!
    }

}

fun Array<String>.findValueOrNull(argName: String) : String? {
    return indexOfFirst { it == argName }.let {
        if (it >= 0) {
            this.getOrNull(it + 1)
        } else {
            null
        }
    }
}