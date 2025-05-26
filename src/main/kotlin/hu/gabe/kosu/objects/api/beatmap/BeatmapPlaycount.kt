package hu.gabe.kosu.objects.api.beatmap

import hu.gabe.kosu.objects.KosuChild
import hu.gabe.kosu.objects.api.beatmapset.Beatmapset
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BeatmapPlaycount(
    @SerialName("beatmap_id") val beatmapId: Long,
    val beatmap: Beatmap? = null,
    val beatmapset: Beatmapset? = null,
    val count: Int
) : KosuChild()