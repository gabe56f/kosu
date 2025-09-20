package hu.gabe.kosu.objects.api.score

import hu.gabe.kosu.annotations.Ignore
import hu.gabe.kosu.annotations.KosuVisitable
import hu.gabe.kosu.child.KosuChild
import hu.gabe.kosu.objects.api.beatmap.Beatmap
import hu.gabe.kosu.objects.api.beatmap.Mods
import hu.gabe.kosu.objects.api.beatmap.ModsSerializer
import hu.gabe.kosu.objects.api.beatmapset.Beatmapset
import hu.gabe.kosu.objects.api.users.User
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@KosuVisitable
data class Score(
    val id: Long,
    val accuracy: Float,
    @SerialName("beatmap_id") val beatmapId: Long,
    @SerialName("ended_at") val endedAt: Instant,
    @SerialName("has_replay") val hasReplay: Boolean,
    @SerialName("is_perfect_combo") val fullCombo: Boolean,
    @SerialName("legacy_perfect") val legacyPerfect: Boolean,
    @SerialName("legacy_total_score") val legacyTotalScore: Int,
    // docs are missing mod object structure???
    val passed: Boolean,
    @SerialName("max_combo") val maxCombo: Int,
    val rank: String,
    @SerialName("total_score") val totalScore: Int,
    val type: String,
    @SerialName("user_id") val userId: Long,
    @Serializable(with = ModsSerializer::class) val mods: Mods = emptyList(),
    val pp: Float? = null,
    @SerialName("classic_total_score") val classicTotalScore: Int? = null,
    @SerialName("build_id") val buildId: Long? = null,
    @SerialName("best_id") val bestId: Long? = null,
    val preserve: Boolean? = null,
    val processed: Boolean? = null,
    val ranked: Boolean? = null,
    @SerialName("room_id") val roomId: Long? = null,
    @SerialName("ruleset_id") val rulesetId: Long? = null,
    @SerialName("started_at") val startedAt: Instant? = null,

    // optional fields
    val beatmap: Beatmap? = null,
    val beatmapset: Beatmapset? = null,
) : KosuChild() {
    val downloadLink: String
        get() = "${kosu.endpointNew}/scores/$id/download"

    @Ignore
    val user: User by lazy { kosu.users[userId] }
}
