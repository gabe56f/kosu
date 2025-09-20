package hu.gabe.kosu.objects.api.beatmap

import hu.gabe.kosu.annotations.KosuVisitable
import hu.gabe.kosu.child.KosuChild
import kotlinx.serialization.Serializable

@Serializable
@KosuVisitable
data class BeatmapOwner(
    val id: Long,
    val username: String
) : KosuChild()
