package hu.gabe.kosu.requests.legacy

import hu.gabe.kosu.Kosu
import hu.gabe.kosu.extensions.decodeFromJson
import hu.gabe.kosu.extensions.get
import hu.gabe.kosu.objects.api.beatmap.Ruleset
import hu.gabe.kosu.objects.legacy.LegacyUser
import hu.gabe.kosu.requests.BaseRequestV1

class UserRequests(kosu: Kosu) : BaseRequestV1(kosu) {
    operator fun get(username: String): LegacyUser = all(username = username).first()

    operator fun get(userId: Long): LegacyUser = all(userId = userId).first()

    fun all(
        username: String? = null,
        mode: Ruleset? = null,
        eventDays: Int? = null,
    ): List<LegacyUser> {
        validate()
        return client.get(
            "$endpoint/get_user", emptyMap(), listOf(
                "k" to "auth_token_here",
                "u" to username,
                "m" to mode?.id?.toString(),
                "type" to "string",
                "event_days" to eventDays?.toString()
            )
        ).decodeFromJson(kosu)
    }

    fun all(
        userId: Long? = null,
        mode: Ruleset? = null,
        eventDays: Int? = null,
    ): List<LegacyUser> {
        validate()
        return client.get(
            "$endpoint/get_user", emptyMap(), listOf(
                "k" to "auth_token_here",
                "u" to userId?.toString(),
                "m" to mode?.id?.toString(),
                "type" to "id",
                "event_days" to eventDays?.toString()
            )
        ).decodeFromJson(kosu)
    }

}