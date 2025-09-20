package hu.gabe.kosu.objects.api.beatmap

import hu.gabe.kosu.annotations.KosuVisitable
import hu.gabe.kosu.child.KosuChild
import hu.gabe.kosu.objects.api.beatmapset.Beatmapset
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@KosuVisitable
data class BeatmapPlaycount(
    @SerialName("beatmap_id") val beatmapId: Long,
    val beatmap: Beatmap? = null,
    val beatmapset: Beatmapset? = null,
    val count: Int
) : KosuChild()