package hu.gabe.kosu.requests.score

import hu.gabe.kosu.Kosu
import hu.gabe.kosu.annotations.RequiresOauth
import hu.gabe.kosu.extensions.decodeFromJson
import hu.gabe.kosu.extensions.get
import hu.gabe.kosu.objects.api.beatmap.Mod
import hu.gabe.kosu.objects.api.beatmap.Ruleset
import hu.gabe.kosu.objects.api.beatmap.BeatmapScores
import hu.gabe.kosu.objects.api.score.Score
import hu.gabe.kosu.requests.BaseRequestV2

class RulesetScoreRequests(kosu: Kosu, private val ruleset: Ruleset) : BaseRequestV2(kosu) {
    @RequiresOauth
    operator fun get(scoreId: Long): Score {
        canExecute()
        return client.get("$endpoint/scores/${ruleset.stringLiteral}/$scoreId").decodeFromJson(kosu)
    }
    
    @RequiresOauth
    @JvmName("get")
    operator fun invoke(cursorString: String? = null): ScoreRequests.Scores {
        canExecute()
        return client.get(
            "$endpoint/scores", emptyMap(), listOf(
                "ruleset" to ruleset.stringLiteral,
                "cursor_string" to cursorString
            )
        ).decodeFromJson(kosu)
    }

    @RequiresOauth
    fun all(): List<Score> {
        canExecute()
        return client.get("$endpoint/scores/${ruleset.stringLiteral}").decodeFromJson<ScoreRequests.Scores>(kosu).scores
    }

    @RequiresOauth
    fun getTopScores(beatmapId: Long, mods: List<Mod> = emptyList(), stableOnly: Boolean = false): BeatmapScores {
        canExecute()
        return client.get("$endpoint/beatmaps/$beatmapId/scores", emptyMap(), listOf(
            "legacy_only" to (if (stableOnly) "1" else "0"),
            *mods.map { "mods[]" to it.acronym }.toTypedArray(),
            "mode" to ruleset.stringLiteral
        )).decodeFromJson(kosu)
    }
}