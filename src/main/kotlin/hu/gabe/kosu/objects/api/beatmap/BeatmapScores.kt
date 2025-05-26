package hu.gabe.kosu.objects.api.beatmap

import hu.gabe.kosu.objects.KosuChild
import hu.gabe.kosu.objects.api.score.Score
import kotlinx.serialization.Serializable

@Serializable
data class BeatmapScores(
    val scores: List<Score>,
    val userScore: BeatmapUserScore? = null
) : KosuChild()