package hu.gabe.kosu.requests.legacy

import hu.gabe.kosu.Kosu
import hu.gabe.kosu.extensions.decodeFromJson
import hu.gabe.kosu.extensions.get
import hu.gabe.kosu.objects.api.beatmap.Mod
import hu.gabe.kosu.objects.api.beatmap.Ruleset
import hu.gabe.kosu.objects.api.beatmap.toIntFlag
import hu.gabe.kosu.objects.legacy.LegacyScore
import hu.gabe.kosu.requests.BaseRequestV1

class ScoreRequests(kosu: Kosu) : BaseRequestV1(kosu) {
    operator fun get(beatmapId: Long): List<LegacyScore> = all(beatmapId = beatmapId, userId = null)

    fun getUserBest(
        userId: Long,
        mode: Ruleset? = null,
        limit: Int? = null,
    ): List<LegacyScore> {
        inRange(limit, 1, 100, "limit")
        return client.get(
            "$endpoint/get_user_best", emptyMap(), listOf(
                "k" to "auth_token_here",
                "u" to userId.toString(),
                "m" to mode?.id?.toString(),
                "type" to "id",
                "limit" to limit?.toString(),
            )
        ).decodeFromJson(kosu)
    }

    fun getUserBest(
        username: String,
        mode: Ruleset? = null,
        limit: Int? = null,
    ): List<LegacyScore> {
        inRange(limit, 1, 100, "limit")
        return client.get(
            "$endpoint/get_user_best", emptyMap(), listOf(
                "k" to "auth_token_here",
                "u" to username,
                "m" to mode?.id?.toString(),
                "type" to "string",
                "limit" to limit?.toString(),
            )
        ).decodeFromJson(kosu)
    }

    fun getUserRecent(
        username: String,
        mode: Ruleset? = null,
        limit: Int? = null,
    ): List<LegacyScore> {
        inRange(limit, 1, 100, "limit")
        return client.get(
            "$endpoint/get_user_recent", emptyMap(), listOf(
                "k" to "auth_token_here",
                "u" to username,
                "m" to mode?.id?.toString(),
                "type" to "string",
                "limit" to limit?.toString(),
            )
        ).decodeFromJson(kosu)
    }

    fun getUserRecent(
        userId: Long,
        mode: Ruleset? = null,
        limit: Int? = null,
    ): List<LegacyScore> {
        inRange(limit, 1, 100, "limit")
        return client.get(
            "$endpoint/get_user_recent", emptyMap(), listOf(
                "k" to "auth_token_here",
                "u" to userId.toString(),
                "m" to mode?.id?.toString(),
                "type" to "id",
                "limit" to limit?.toString(),
            )
        ).decodeFromJson(kosu)
    }

    fun all(
        beatmapId: Long,
        userId: Long? = null,
        mode: Ruleset? = null,
        mods: List<Mod>? = null,
        limit: Int? = null
    ): List<LegacyScore> {
        inRange(limit, 1, 100, "limit")
        val modsInt = mods.toIntFlag()

        return client.get(
            "$endpoint/get_scores", emptyMap(), listOf(
                "k" to "auth_token_here",
                "b" to beatmapId.toString(),
                "u" to userId?.toString(),
                "m" to mode?.id?.toString(),
                "type" to "id",
                "mods" to modsInt?.toString(),
                "limit" to limit?.toString(),
            )
        ).decodeFromJson(kosu)
    }

    fun all(
        beatmapId: Long,
        username: String? = null,
        mode: Ruleset? = null,
        mods: List<Mod>? = null,
        limit: Int? = null
    ): List<LegacyScore> {
        inRange(limit, 1, 100, "limit")
        val modsInt = mods.toIntFlag()

        return client.get(
            "$endpoint/get_scores", emptyMap(), listOf(
                "k" to "auth_token_here",
                "b" to beatmapId.toString(),
                "u" to username,
                "m" to mode?.id?.toString(),
                "type" to "string",
                "mods" to modsInt?.toString(),
                "limit" to limit?.toString(),
            )
        ).decodeFromJson(kosu)
    }
}