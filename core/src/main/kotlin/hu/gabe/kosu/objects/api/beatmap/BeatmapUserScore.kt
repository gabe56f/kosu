package hu.gabe.kosu.objects.api.beatmap

import hu.gabe.kosu.annotations.KosuVisitable
import hu.gabe.kosu.child.KosuChild
import hu.gabe.kosu.objects.api.score.Score
import kotlinx.serialization.Serializable

@Serializable
@KosuVisitable
data class BeatmapUserScore(
    val position: Int,
    val score: Score
) : KosuChild()
