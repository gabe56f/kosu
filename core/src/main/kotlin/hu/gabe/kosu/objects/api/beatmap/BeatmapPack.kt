package hu.gabe.kosu.objects.api.beatmap

import hu.gabe.kosu.annotations.KosuVisitable
import hu.gabe.kosu.child.KosuChild
import hu.gabe.kosu.objects.api.beatmapset.Beatmapset
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@KosuVisitable
data class BeatmapPack(
    val author: String,
    val date: Instant,
    val name: String,
    @SerialName("no_diff_reduction") val noDiffReduction: Boolean,
    // @SerialName("ruleset_id") val rulesetid: Long,
    val tag: String,
    val url: String,

    // optional attributes
    val beatmapsets: List<Beatmapset>? = emptyList(),
    @SerialName("user_completion_data") val userData: PackCompletionData? = null,
) : KosuChild()

@Serializable
data class PackCompletionData(
    @SerialName("beatmapset_ids") val beatmapsetIds: List<Int> = emptyList(),
    val completed: Boolean? = null,
)