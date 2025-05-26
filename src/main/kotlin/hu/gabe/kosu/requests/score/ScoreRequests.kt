package hu.gabe.kosu.requests.score

import hu.gabe.kosu.Kosu
import hu.gabe.kosu.annotations.RequiresOauth
import hu.gabe.kosu.extensions.decodeFromJson
import hu.gabe.kosu.extensions.get
import hu.gabe.kosu.objects.api.beatmap.Ruleset
import hu.gabe.kosu.objects.api.beatmap.BeatmapScores
import hu.gabe.kosu.objects.api.beatmap.BeatmapUserScore
import hu.gabe.kosu.objects.api.score.Score
import hu.gabe.kosu.requests.BaseRequestV2
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class ScoreRequests(kosu: Kosu) : BaseRequestV2(kosu) {
    @get:JvmName("standard")
    val standard = RulesetScoreRequests(kosu, Ruleset.STANDARD)

    @get:JvmName("taiko")
    val taiko = RulesetScoreRequests(kosu, Ruleset.TAIKO)

    @get:JvmName("ctb")
    val ctb = RulesetScoreRequests(kosu, Ruleset.CTB)

    @get:JvmName("mania")
    val mania = RulesetScoreRequests(kosu, Ruleset.MANIA)

    @RequiresOauth
    fun getUserScoreForBeatmap(beatmapId: Long, userId: Long): BeatmapUserScore {
        canExecute()
        return client.get(
            "$endpoint/{beatmap}/scores/users/{user}", mapOf(
                "beatmap" to beatmapId.toString(),
                "user" to userId.toString()
            )
        ).decodeFromJson(kosu)
    }

    @RequiresOauth
    fun getUserBeatmapScores(beatmapId: Long, userId: Long): Scores {
        canExecute()
        return client.get(
            "$endpoint/{beatmap}/scores/users/{user}/all", mapOf(
                "beatmap" to beatmapId.toString(),
                "user" to userId.toString()
            )
        ).decodeFromJson(kosu)
    }

    @RequiresOauth
    fun getBeatmapScores(beatmapId: Long): BeatmapScores {
        canExecute()
        return client.get(
            "$endpoint/{beatmap}/scores", mapOf(
                "beatmap" to beatmapId.toString()
            )
        ).decodeFromJson(kosu)
    }

    @RequiresOauth
    operator fun get(scoreId: Long): Score {
        canExecute()
        return client.get("$endpoint/scores/$scoreId").decodeFromJson(kosu)
    }

    @Serializable
    data class Scores(val scores: List<Score>, @SerialName("cursor_string") val cursorString: String? = null)
}