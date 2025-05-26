package hu.gabe.kosu.auth

import hu.gabe.kosu.Kosu
import hu.gabe.kosu.extensions.post
import hu.gabe.kosu.objects.api.users.User
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.time.Duration.Companion.seconds

/**
 * Client-based authentication method for osu!v2 api.
 *
 * Doesn't support the [Scope.DELEGATE] scope.
 */
class AuthorizationCodeGrant(
    private val kosu: Kosu,
    private val clientId: Int,
    private val clientSecret: String,
    private val redirectUri: String,
    scope: List<Scope>
) : KosuAuthenticator(kosu) {
    private val authenticatorClient = kosu.newClient()
    private val endpoint by kosu::oauthEndpoint
    private val json by kosu::json

    var expiresAt: Instant? = null
        private set
    private var code: String? = null
    private var lastToken: String? = null
    private var refreshToken: String? = null

    val authflow: AuthFlow by lazy {
        AuthFlow(
            "$endpoint/authorize?client_id=$clientId&redirect_uri=$redirectUri&response_type=code&scope=${
                scopes.joinToString(
                    "%20"
                ) { it.json }
            }"
        ) { code -> this.code = code }
    }

    override var scopes: List<Scope> = scope
        set(value) {
            if (value.isEmpty()) throw IllegalArgumentException("At least one scope must be provided.")
            if (value.any { it !in field }) throw IllegalArgumentException("Authorization grants don't support requesting more scopes, just less.")

            // refresh with fewer scopes
            if (value.hashCode() != field.hashCode()) expiresAt = null
            field = value
        }

    override val client: OkHttpClient by lazy {
        kosu.newClient {
            addInterceptor {
                if (token != null) {
                    val request = it.request()
                        .newBuilder()
                        .addHeader("Authorization", "Bearer $token")
                        .build()
                    it.proceed(request)
                } else it.proceed(it.request())
            }
        }
    }

    override val token: String?
        get() {
            if (code == null) return null

            val refresh = refreshToken != null && (expiresAt != null && Clock.System.now() > expiresAt!!)
            val call = if (expiresAt == null) {
                authenticatorClient.post(
                    "$endpoint/token",
                    mapOf(
                        Pair("grant_type", "authorization_code"),
                        Pair("client_id", clientId.toString()),
                        Pair("client_secret", clientSecret),
                        Pair("code", code!!),
                        Pair("redirect_uri", redirectUri)
                    )
                )
            } else if (refresh) {
                authenticatorClient.post(
                    "$endpoint/token",
                    mapOf(
                        Pair("grant_type", "refresh_token"),
                        Pair("client_id", clientId.toString()),
                        Pair("client_secret", clientSecret),
                        Pair("refresh_token", refreshToken!!),
                        Pair("scope", scopes.joinToString(" ") { it.json })
                    )
                )
            } else return lastToken!!

            val response = call.execute()
            val token = json.decodeFromString<ClientTokenResponse>(response.body.string())

            expiresAt = Clock.System.now() + token.expiresInSeconds.seconds
            lastToken = token.accessToken
            refreshToken = token.refreshToken
            return lastToken!!
        }

    /**
     * Requests the user associated with the current token.
     *
     * This method requires the [Scope.IDENTIFY] scope.
     */
    override val user: User?
        get() {
            if (Scope.IDENTIFY in scopes)
                return kosu.users.me()
            return null
        }

    override fun deauth(): Boolean {
        val call = client.newCall(
            Request.Builder()
                .url("${kosu.endpointNew}/oauth/tokens/current")
                .delete()
                .build()
        )
        val response = call.execute()
        return response.code == 204
    }

    class AuthFlow(val url: String, internal val callback: (String) -> Unit) {
        fun code(code: String) = callback(code)
    }

    @Serializable
    data class ClientTokenResponse(
        @SerialName("token_type") val tokenType: String,
        @SerialName("access_token") val accessToken: String,
        @SerialName("refresh_token") val refreshToken: String,
        @SerialName("expires_in") val expiresInSeconds: Int,
    )
}