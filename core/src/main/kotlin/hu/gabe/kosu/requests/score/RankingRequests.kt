package hu.gabe.kosu.requests.score

import hu.gabe.kosu.Kosu
import hu.gabe.kosu.annotations.RequiresOauth
import hu.gabe.kosu.extensions.decodeFromJson
import hu.gabe.kosu.extensions.get
import hu.gabe.kosu.objects.api.beatmap.Ruleset
import hu.gabe.kosu.objects.api.score.RankingFilter
import hu.gabe.kosu.objects.api.score.RankingType
import hu.gabe.kosu.objects.api.score.Rankings
import hu.gabe.kosu.objects.api.score.Spotlights
import hu.gabe.kosu.requests.BaseRequestV2

class RankingRequests(kosu: Kosu) : BaseRequestV2(kosu) {
    @RequiresOauth
    fun spotlights(): Spotlights = client.get("$endpoint/spotlights").decodeFromJson(kosu)

    @RequiresOauth
    fun all(
        ruleset: Ruleset,
        type: RankingType,
        country: String? = null,
        filter: RankingFilter? = null,
        spotlight: String? = null,
        variant: String? = null,
    ): Rankings = client.get(
        "$endpoint/rankings/${ruleset.stringLiteral}/${type.stringLiteral}", emptyMap(), listOf(
            "country" to country,
            "variant" to variant,
            "spotlight" to spotlight,
            "filter" to filter?.stringLiteral,
        )
    ).decodeFromJson(kosu)
}