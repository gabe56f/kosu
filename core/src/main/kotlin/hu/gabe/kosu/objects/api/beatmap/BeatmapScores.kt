package hu.gabe.kosu.objects.api.beatmap

import hu.gabe.kosu.annotations.KosuVisitable
import hu.gabe.kosu.child.KosuChild
import hu.gabe.kosu.objects.api.score.Score
import kotlinx.serialization.Serializable

@Serializable
@KosuVisitable
data class BeatmapScores(
    val scores: List<Score>,
    val userScore: BeatmapUserScore? = null
) : KosuChild()