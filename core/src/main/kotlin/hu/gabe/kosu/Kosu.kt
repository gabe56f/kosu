package hu.gabe.kosu

import com.mayakapps.kache.InMemoryKache
import com.mayakapps.kache.KacheStrategy
import hu.gabe.kosu.auth.*
import hu.gabe.kosu.requests.SearchRequests
import hu.gabe.kosu.requests.beatmaps.BeatmapPackRequests
import hu.gabe.kosu.requests.beatmaps.BeatmapRequests
import hu.gabe.kosu.requests.beatmaps.BeatmapsetRequests
import hu.gabe.kosu.requests.changelog.ChangelogRequests
import hu.gabe.kosu.requests.comments.CommentRequests
import hu.gabe.kosu.requests.forums.ForumRequests
import hu.gabe.kosu.requests.legacy.V1Requests
import hu.gabe.kosu.requests.multiplayer.MatchRequests
import hu.gabe.kosu.requests.multiplayer.RoomRequests
import hu.gabe.kosu.requests.news.NewsRequests
import hu.gabe.kosu.requests.score.RankingRequests
import hu.gabe.kosu.requests.score.ScoreRequests
import hu.gabe.kosu.requests.users.UserRequests
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import kotlin.time.Duration.Companion.seconds

/**
 * Kosu is an osu!v2 api wrapper
 *
 * Functions marked with [hu.gabe.kosu.annotations.RequiresOauth] require an authenticated [Kosu] object.
 *
 * @author Gábe
 */
class Kosu(
    val endpoint: String = "https://osu.ppy.sh/api",
    val oauthEndpoint: String = "https://osu.ppy.sh/oauth",
    val apiVersion: String = "20240529",
    private val clientFactory: () -> OkHttpClient.Builder = { OkHttpClient.Builder() },
    private val jsonFactory: () -> Json = {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
    },
    private val scopes: List<Scope> = emptyList(),
    private val userAgent: String = "Kosu/1.0",
    cacheExpirySeconds: Long = 60 * 10, // 10 minutes
) {
    var auth: KosuAuthenticator = NoAuth(this)
    internal val client
        get() = auth.client
    internal val json by lazy {
        jsonFactory()
    }
    internal lateinit var legacyHandler: LegacyHandler
    internal val endpointNew = "$endpoint/v2"
    internal val cache = InMemoryKache<String, Any>(maxSize = 128) {
        strategy = KacheStrategy.LRU
        expireAfterWriteDuration = cacheExpirySeconds.seconds
    }

    /**
     * The authentication state of this [Kosu] object. To be authenticated, the authentication method must not be [NoAuth] and needs to have it's token ready.
     *
     * Eg.: [AuthorizationCodeGrant] will not be authenticated unless the authflow is completed.
     */
    val authenticated
        get() = auth !is NoAuth && auth.token != null

    /**
     * Whether this [Kosu] object is ready to call v1 (legacy) endpoints.
     */
    val legacyReady
        get() = ::legacyHandler.isInitialized

    internal fun newClient(clientBuilder: (OkHttpClient.Builder.() -> OkHttpClient.Builder)? = null): OkHttpClient {
        return (clientBuilder ?: { this })(clientFactory())
            .addInterceptor {
                val request = it.request()
                    .newBuilder()
                    .addHeader("Accept", "application/json")
                    .addHeader("Content-Type", "application/json")
                    .removeHeader("User-Agent")
                    .addHeader("User-Agent", userAgent)
                    .addHeader("x-api-version", apiVersion)
                it.proceed(request.build())
            }.build()
    }

    /**
     * Authorize this [Kosu] instance with the provided [clientId] and [clientSecret]. The authentication method will be a [ClientCredentialsGrant].
     */
    fun login(clientId: Int, clientSecret: String): Kosu = apply {
        auth = ClientCredentialsGrant(this, clientId, clientSecret, scopes)
    }

    /**
     * Authorize this [Kosu] instance with the provided [clientId], [clientSecret], [redirectUri], and [scopes]. The authentication method will be a [AuthorizationCodeGrant].
     *
     * Example authentication procedure:
     * ```kotlin
     * val (kosu, authflow) = Kosu().login(CLIENT_ID, CLIENT_SECRET, REDIRECT_URI)
     * val url = authflow.url
     * // open url in browser and authenticate
     * val CODE = ... // received from user
     * authflow.code(CODE)
     * // Authenticated!
     * ```
     */
    fun login(clientId: Int, clientSecret: String, redirectUri: String): Pair<Kosu, AuthorizationCodeGrant.AuthFlow> {
        auth = AuthorizationCodeGrant(this, clientId, clientSecret, redirectUri, scopes)
        return this to (auth as AuthorizationCodeGrant).authflow
    }

    /**
     * Authenticate the requests to the old (/legacy) osu! api.
     */
    fun authenticate(token: String): Kosu = apply {
        this.legacyHandler = LegacyHandler(this, token)
    }

    /**
     * Deauthorize this [Kosu] instance. Resets the [auth] property to [NoAuth].
     *
     * If this instance was not authenticated, this method is a no-op and returns `true`.
     */
    fun deauth(): Boolean {
        if (!authenticated) return true
        return auth.deauth().also { auth = NoAuth(this) }
    }

    @get:JvmName("users")
    val users = UserRequests(this)

    @get:JvmName("beatmaps")
    val beatmaps = BeatmapRequests(this)

    @get:JvmName("beatmapsets")
    val beatmapsets: BeatmapsetRequests = BeatmapsetRequests(this)

    @get:JvmName("scores")
    val scores = ScoreRequests(this)

    @get:JvmName("packs")
    val packs = BeatmapPackRequests(this)

    @get:JvmName("changelogs")
    val changelogs = ChangelogRequests(this)

    @get:JvmName("comments")
    val comments = CommentRequests(this)

    @get:JvmName("forums")
    val forums = ForumRequests(this)

    @get:JvmName("news")
    val news = NewsRequests(this)

    @get:JvmName("search")
    val search = SearchRequests(this)

    @get:JvmName("matches")
    val matches = MatchRequests(this)

    @get:JvmName("rooms")
    val rooms = RoomRequests(this)

    @get:JvmName("rankings")
    val rankings = RankingRequests(this)

    @get:JvmName("legacy")
    val legacy = V1Requests(this)


    class Builder(constructor: Builder.() -> Unit) {
        private val scopes: MutableList<Scope> = ArrayList(4)
        var endpoint: String = "https://osu.ppy.sh/api"
            private set(value) {
                val newValue = if (value.endsWith("/"))
                    value.removeSuffix("/")
                else value
                field = newValue
            }
        var oauthEndpoint: String = "https://osu.ppy.sh/oauth"
            private set(value) {
                val newValue = if (value.endsWith("/"))
                    value.removeSuffix("/")
                else value
                field = newValue
            }
        var apiVersion: String = "20240529"
            private set
        var clientFactory: () -> OkHttpClient.Builder = { OkHttpClient.Builder() }
            private set
        var jsonFactory: () -> Json = {
            Json {
                ignoreUnknownKeys = true
                isLenient = true
            }
        }
            private set
        var userAgent: String = "Kosu/1.0"
            private set

        fun endpoint(endpoint: String) = apply { this.endpoint = endpoint }
        fun oauthEndpoint(endpoint: String) = apply { this.oauthEndpoint = endpoint }
        fun apiVersion(apiVersion: String) = apply { this.apiVersion = apiVersion }
        fun clientFactory(factory: () -> OkHttpClient.Builder) = apply { this.clientFactory = factory }
        fun jsonFactory(factory: () -> Json) = apply { this.jsonFactory = factory }
        fun userAgent(userAgent: String) = apply { this.userAgent = userAgent }
        fun scope(scope: Scope) = apply { this.scopes.add(scope) }
        fun scopes(scopes: List<Scope>) = apply { this.scopes.addAll(scopes) }
        fun removeScope(scope: Scope) = apply { this.scopes.remove(scope) }
        fun clearScopes() = apply { this.scopes.clear() }

        init {
            this.constructor()
        }

        fun build(): Kosu = Kosu(endpoint, oauthEndpoint, apiVersion, clientFactory, jsonFactory, scopes.distinct())

        constructor() : this({})
    }

}