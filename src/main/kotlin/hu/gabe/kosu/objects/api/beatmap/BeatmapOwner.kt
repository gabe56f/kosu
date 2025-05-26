package hu.gabe.kosu.objects.api.beatmap

import hu.gabe.kosu.objects.KosuChild
import kotlinx.serialization.Serializable

@Serializable
data class BeatmapOwner(
    val id: Long,
    val username: String
) : KosuChild()
