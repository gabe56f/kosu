package hu.gabe.kosu.objects.api.beatmap

import hu.gabe.kosu.objects.KosuChild
import hu.gabe.kosu.objects.api.score.Score
import kotlinx.serialization.Serializable

@Serializable
data class BeatmapUserScore(
    val position: Int,
    val score: Score
) : KosuChild()
