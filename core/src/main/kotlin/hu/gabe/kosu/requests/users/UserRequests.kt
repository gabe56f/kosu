package hu.gabe.kosu.requests.users

import hu.gabe.kosu.Kosu
import hu.gabe.kosu.Scope
import hu.gabe.kosu.annotations.RequiresOauth
import hu.gabe.kosu.extensions.decodeFromJson
import hu.gabe.kosu.extensions.get
import hu.gabe.kosu.objects.api.beatmap.Ruleset
import hu.gabe.kosu.objects.api.event.Event
import hu.gabe.kosu.objects.api.users.User
import hu.gabe.kosu.requests.BaseRequestV2
import kotlinx.serialization.Serializable

class UserRequests(kosu: Kosu) : BaseRequestV2(kosu) {
    val url = "$endpoint/users/{user}/{mode?}"

    @get:JvmName("standard")
    val standard = RulesetUserRequests(kosu, Ruleset.STANDARD)

    @get:JvmName("taiko")
    val taiko = RulesetUserRequests(kosu, Ruleset.TAIKO)

    @get:JvmName("ctb")
    val ctb = RulesetUserRequests(kosu, Ruleset.CTB)

    @get:JvmName("mania")
    val mania = RulesetUserRequests(kosu, Ruleset.MANIA)

    @RequiresOauth
    operator fun get(userId: Long): User = this(userId)

    @RequiresOauth
    operator fun get(username: String): User = this(username)

    @JvmName("getByIds")
    @RequiresOauth
    operator fun get(userIds: List<Int>): List<User> {
        if (userIds.isEmpty()) return emptyList()
        if (userIds.size > 50) throw IllegalArgumentException("Cannot request more than 50 users at once.")

        return client.get("$endpoint/users", emptyMap(), userIds.map { "ids[]" to it.toString() })
            .decodeFromJson<Users>(kosu).users
    }

    @JvmName("getByUsernames")
    @RequiresOauth
    operator fun get(usernames: List<String>): List<User> {
        if (usernames.isEmpty()) return emptyList()
        if (usernames.size > 50) throw IllegalArgumentException("Cannot request more than 50 users at once.")

        return client.get(
            "$endpoint/users",
            emptyMap(),
            usernames.map { if (it.startsWith("@")) it else "@$it" }.map { "usernames[]" to it })
            .decodeFromJson<Users>(kosu).users
    }

    @JvmName("get")
    @RequiresOauth
    operator fun invoke(username: String, mode: Ruleset? = null): User {
        val user = if (username.startsWith("@")) username else "@$username"

        return client.get(url, mapOf("user" to user, "mode" to mode?.stringLiteral))
            .decodeFromJson(kosu)
    }

    @JvmName("get")
    @RequiresOauth
    operator fun invoke(userId: Long, mode: Ruleset? = null): User =
        client.get(url, mapOf("user" to userId.toString(), "mode" to mode?.stringLiteral))
            .decodeFromJson(kosu)

    @RequiresOauth(Scope.PUBLIC, Scope.IDENTIFY)
    fun me(mode: Ruleset? = null): User =
        client.get("$endpoint/me/{mode?}", mapOf("mode" to mode?.stringLiteral)).decodeFromJson(kosu)

    @RequiresOauth
    fun getRecentUserActivity(userId: Long): List<Event> =
        client.get("$endpoint/users/$userId/recent_activity", emptyMap(), emptyList()).decodeFromJson(kosu)

    @Serializable
    data class Users(val users: List<User>)
}