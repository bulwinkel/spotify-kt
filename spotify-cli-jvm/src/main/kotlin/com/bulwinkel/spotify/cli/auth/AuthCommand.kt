package com.bulwinkel.spotify.cli.auth

import com.bulwinkel.spotify.auth.SpotifyAuthClient
import com.bulwinkel.spotify.auth.models.SpotifyApp
import com.bulwinkel.spotify.cli.Command
import com.bulwinkel.spotify.cli.authCacheFile
import com.squareup.moshi.Moshi
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

        val parsedArgs = AuthCommandArgs(args).apply {
            Fire.d { "clientId = $clientId" }
            Fire.d { "clientSecret = $clientSecret" }
            Fire.d { "scope = $scope" }
        }

        val code = parsedArgs.run {
            promptUserForToken(
                    clientId = clientId,
                    redirectUri = redirectUri,
                    scope = scope
            )
        }

        Fire.d { "authCode = $code" }

        val authClient = SpotifyAuthClient(SpotifyApp(
                clientId = parsedArgs.clientId,
                clientSecret = parsedArgs.clientSecret,
                redirectUri = parsedArgs.redirectUri
        ))

        val token = authClient.token(code).blockingGet()
        val authCache = SpotifyAuthCache(
                clientId = parsedArgs.clientId,
                clientSecret = parsedArgs.clientSecret,
                redirectUri = parsedArgs.redirectUri,
                tokenType = token.token_type,
                accessToken = token.access_token,
                scope = token.scope,
                // take 5 seconds from the seconds stated by the api
                expiresAt = System.currentTimeMillis() + ((token.expires_in - 5) * 1000),
                refreshToken = token.refresh_token
        )

        Moshi.Builder().build()
                .adapter(SpotifyAuthCache::class.java)
                .indent("  ")
                .toJson(authCache)
                .let { json ->
                    Fire.d { "authCacheJson = $json" }
                    authCacheFile.writeText(json)
                }
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

    val code = actualCallbackUrl.split("code=").last().split("&").first()
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
    val scope by arg(args, { emptyList<String>() }) { it.split(",") }
}

private val stringValueTransformer: (String) -> String = { it }

fun stringArg(args: Array<String>, defaultValue: (argName: String) -> String) =
        ArgDelegate(args, defaultValue, stringValueTransformer)

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