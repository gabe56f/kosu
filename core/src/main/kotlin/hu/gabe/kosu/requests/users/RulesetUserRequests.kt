package hu.gabe.kosu.requests.users

import hu.gabe.kosu.Kosu
import hu.gabe.kosu.Scope
import hu.gabe.kosu.annotations.RequiresOauth
import hu.gabe.kosu.extensions.decodeFromJson
import hu.gabe.kosu.extensions.get
import hu.gabe.kosu.objects.api.beatmap.Ruleset
import hu.gabe.kosu.objects.api.users.User
import hu.gabe.kosu.requests.BaseRequestV2

class RulesetUserRequests(kosu: Kosu, private val ruleset: Ruleset) : BaseRequestV2(kosu) {

    @RequiresOauth
    operator fun get(userId: Long): User =
        client.get("$endpoint/users/$userId/${ruleset.stringLiteral}", emptyMap(), listOf("key" to "id"))
            .decodeFromJson(kosu)

    @RequiresOauth
    operator fun get(username: String): User =
        client.get("$endpoint/users/$username/${ruleset.stringLiteral}", emptyMap(), listOf("key" to "username"))
            .decodeFromJson(kosu)

    @RequiresOauth(Scope.PUBLIC, Scope.IDENTIFY)
    fun me(): User = client.get("$endpoint/me/${ruleset.stringLiteral}").decodeFromJson(kosu)

}