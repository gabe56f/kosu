package hu.gabe.kosu.requests.legacy

import hu.gabe.kosu.Kosu
import hu.gabe.kosu.extensions.decodeFromJson
import hu.gabe.kosu.extensions.get
import hu.gabe.kosu.objects.api.beatmap.Mod
import hu.gabe.kosu.objects.api.beatmap.Ruleset
import hu.gabe.kosu.objects.api.beatmap.toIntFlag
import hu.gabe.kosu.objects.legacy.LegacyBeatmap
import hu.gabe.kosu.requests.BaseRequestV1
import kotlinx.datetime.Instant


class BeatmapRequests(kosu: Kosu) : BaseRequestV1(kosu) {
    operator fun get(beatmapId: Long): LegacyBeatmap = all(beatmapId = beatmapId, limit = 1, userId = null).first()

    fun all(
        since: Instant? = null,
        beatmapsetId: Long? = null,
        beatmapId: Long? = null,
        userId: Long? = null,
        mode: Ruleset? = null,
        includeConverted: Boolean? = null,
        checksum: String? = null,
        limit: Int? = null,
        mods: List<Mod>? = null,
    ): List<LegacyBeatmap> {
        inRange(limit, 1, 500, "limit")
        val modsInt = mods.toIntFlag()

        return client.get(
            "$endpoint/get_beatmaps", emptyMap(), listOf(
                "k" to "auth_token_here",
                "since" to since?.toString(),
                "s" to beatmapsetId?.toString(),
                "b" to beatmapId?.toString(),
                "u" to userId?.toString(),
                "type" to "id",
                "m" to mode?.id?.toString(),
                "a" to includeConverted?.let { if (it) "1" else "0" },
                "h" to checksum,
                "limit" to limit?.toString(),
                "mods" to modsInt?.toString()
            )
        ).decodeFromJson(kosu)
    }


    fun all(
        since: Instant? = null,
        beatmapsetId: Long? = null,
        beatmapId: Long? = null,
        username: String? = null,
        mode: Ruleset? = null,
        includeConverted: Boolean? = null,
        checksum: String? = null,
        limit: Int? = null,
        mods: List<Mod>? = null,
    ): List<LegacyBeatmap> {
        inRange(limit, 1, 500, "limit")
        val modsInt = mods.toIntFlag()

        return client.get(
            "$endpoint/get_beatmaps", emptyMap(), listOf(
                "k" to "auth_token_here",
                "since" to since?.toString(),
                "s" to beatmapsetId?.toString(),
                "b" to beatmapId?.toString(),
                "u" to username,
                "type" to "string",
                "m" to mode?.id?.toString(),
                "a" to includeConverted?.let { if (it) "1" else "0" },
                "h" to checksum,
                "limit" to limit?.toString(),
                "mods" to modsInt?.toString()
            )
        ).decodeFromJson(kosu)
    }
}