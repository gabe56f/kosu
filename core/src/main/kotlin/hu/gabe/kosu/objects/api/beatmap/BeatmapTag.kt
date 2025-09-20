package hu.gabe.kosu.objects.api.beatmap

import hu.gabe.kosu.annotations.KosuVisitable
import hu.gabe.kosu.child.KosuChild
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@KosuVisitable
data class BeatmapTag(
    val id: String,
    val name: String,
    val description: String,
    @SerialName("ruleset_id") val rulesetId: Long = 0,
) : KosuChild()