package hu.gabe.kosu.auth

import hu.gabe.kosu.Kosu
import hu.gabe.kosu.extensions.post
import hu.gabe.kosu.objects.api.users.User
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.MissingFieldException
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.time.Duration.Companion.seconds

/**
 * Userless authentication method for osu!v2 api.
 *
 * Only supports the [Scope.PUBLIC] and [Scope.DELEGATE] scopes.
 */
class ClientCredentialsGrant(
    private val kosu: Kosu,
    private val clientId: Int,
    private val clientSecret: String,
    scope: List<Scope>
) : KosuAuthenticator(kosu) {
    private val authenticatorClient = kosu.newClient()
    private val endpoint by kosu::oauthEndpoint
    private val json by kosu::json

    var expiresAt: Instant? = null
        private set
    private var lastToken: String? = null

    override var scopes: List<Scope> = scope
        set(value) {
            value.all {
                it in listOf(
                    Scope.PUBLIC,
                    Scope.DELEGATE
                )
            } || throw IllegalArgumentException("Only PUBLIC and DELEGATE scopes are allowed for ClientCredentialsGrant.")
            if (value.isEmpty()) throw IllegalArgumentException("At least one scope must be provided.")

            // if `field` has changed, reset the token
            if (value.hashCode() != field.hashCode()) expiresAt = null
            field = value
        }

    override val client: OkHttpClient by lazy {
        kosu.newClient {
            addInterceptor {
                val request = it.request()
                    .newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                it.proceed(request)
            }
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    override val token: String
        get() {
            val refresh = expiresAt == null || (expiresAt != null && Clock.System.now() > expiresAt!!)

            if (refresh) {
                val call = authenticatorClient.post(
                    "$endpoint/token",
                    mapOf(
                        Pair("grant_type", "client_credentials"),
                        Pair("client_id", clientId.toString()),
                        Pair("client_secret", clientSecret),
                        Pair("scope", scopes.joinToString(" ") { it.json })
                    )
                )
                val response = call.execute()
                val token = try {
                    json.decodeFromString<TokenResponse>(response.body.string())
                } catch (_: MissingFieldException) {
                    scopes = listOf(Scope.PUBLIC)
                    val call = authenticatorClient.post(
                        "$endpoint/token",
                        mapOf(
                            Pair("grant_type", "client_credentials"),
                            Pair("client_id", clientId.toString()),
                            Pair("client_secret", clientSecret),
                            Pair("scope", scopes.joinToString(" ") { it.json })
                        )
                    )
                    val response = call.execute()
                    json.decodeFromString<TokenResponse>(response.body.string())
                }
                expiresAt = Clock.System.now() + token.expiresInSeconds.seconds
                lastToken = token.accessToken
                return lastToken!!
            } else return lastToken!!
        }

    /**
     * Not supported for this authentication method.
     */
    override val user: User? = null

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

    @Serializable
    data class TokenResponse(
        @SerialName("token_type") val tokenType: String,
        @SerialName("access_token") val accessToken: String,
        @SerialName("expires_in") val expiresInSeconds: Int,
    )
}