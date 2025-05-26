package hu.gabe.kosu.objects.api.beatmapset

import hu.gabe.kosu.objects.api.beatmap.Ruleset
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Nomination(
    @SerialName("beatmapset_id") val beatmapsetId: Long,
    val rulesets: List<Ruleset>,
    val reset: Boolean,
    @SerialName("user_id") val userId: Long
)
