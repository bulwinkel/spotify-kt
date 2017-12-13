package com.bulwinkel.spotify.cli.auth

import com.bulwinkel.spotify.cli.Command
import fire.log.Fire
import okhttp3.HttpUrl
import java.util.Scanner
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

object AuthCommand : Command {

    override val description: String =
            "Authenticates with spotify saving authentication details to your home directory"

    override fun action(args: Array<String>) {
        Fire.d { "AuthCommand args: ${args.toList()}" }

        val code = AuthCommandArgs(args).run {
            Fire.d { "clientId = $clientId" }
            Fire.d { "clientSecret = $clientSecret" }
            Fire.d { "scope = $scope" }
            promptUserForToken(
                    clientId = clientId,
                    redirectUri = redirectUri,
                    scope = scope
            )
        }

        Fire.d { "authCode = $code" }
    }
}

private fun promptUserForToken(
        clientId: String,
        redirectUri: String,
        scope: List<String> = emptyList(),
        callbackUrl:String? = null
) : String {

    val urlBuilder = HttpUrl.Builder()
            .scheme("https")
            .host("accounts.spotify.com")
            .addPathSegment("authorize")
            .addQueryParameter("client_id", clientId)
            .addQueryParameter("response_type", "code")
            .addQueryParameter("redirect_uri", redirectUri)
            .addQueryParameter("show_dialog", true.toString())

    val scope = if (scope.isNotEmpty()) scope.joinToString(" ") else null
    if (scope != null) {
        urlBuilder.addQueryParameter("scope", scope)
    }

    val actualCallbackUrl = if (callbackUrl != null) {
        callbackUrl
    } else {
        val url = urlBuilder.build()
        Runtime.getRuntime().exec("open $url")
        println("enter the callback url displayed in the browser:")
        val scanner = Scanner(System.`in`)
        scanner.next()
    }

    val code = actualCallbackUrl.split(delimiters = "code=").last().split(delimiters = "&").first()
    println("code = $code")

    return code
}

class AuthCommandArgs(args: Array<String>) {

    private fun readLineOrThrow(prompt: String) : (argName: String) -> String {
        return {
            println(prompt)
            readLine() ?: throw NullPointerException()
        }
    }

    val clientId by stringArg(args, readLineOrThrow("Enter Client ID:"))
    val clientSecret by stringArg(args, readLineOrThrow("Enter Client Secret:"))
    val redirectUri by stringArg(args, readLineOrThrow("Enter RedirectUri:"))
    val scope by arg(args, { emptyList<String>() }) { it.split(",")}
}

private val stringValueTransformer: (String) -> String = { it }

fun stringArg(args: Array<String>, defaultValue: (argName: String) -> String) : ArgDelegate<String> {
    return ArgDelegate(args, defaultValue, stringValueTransformer)
}

fun <T> arg(
        args: Array<String>,
        defaultValue: (argName: String) -> T,
        valueTransformer: (value: String) -> T
) = ArgDelegate(args, defaultValue, valueTransformer)

class ArgDelegate<out T>(
        private val args: Array<String>,
        private val defaultValue: (argName: String) -> T,
        private val valueTransformer: (value: String) -> T
) : ReadOnlyProperty<AuthCommandArgs, T> {

    private var value: T? = null

    override fun getValue(thisRef: AuthCommandArgs, property: KProperty<*>): T {
        if (value == null) {
            val propName = property.name
            Fire.d {  "looking up value for property.name = $propName" }
            value = args.findValueOrNull("--$propName")?.let(valueTransformer) ?: defaultValue(propName)
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