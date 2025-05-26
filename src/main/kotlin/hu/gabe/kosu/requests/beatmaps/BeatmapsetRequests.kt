package hu.gabe.kosu.requests.beatmaps

import hu.gabe.kosu.Kosu
import hu.gabe.kosu.annotations.RequiresOauth
import hu.gabe.kosu.extensions.decodeFromJson
import hu.gabe.kosu.extensions.get
import hu.gabe.kosu.objects.api.beatmapset.Beatmapset
import hu.gabe.kosu.objects.api.beatmapset.BeatmapsetSearch
import hu.gabe.kosu.objects.api.beatmapset.BeatmapsetSearchResult
import hu.gabe.kosu.requests.BaseRequestV2

class BeatmapsetRequests(kosu: Kosu) : BaseRequestV2(kosu) {

    @get:JvmName("discussions")
    val discussions: BeatmapsetDiscussionRequests = BeatmapsetDiscussionRequests(kosu)

    @RequiresOauth
    operator fun get(beatmapsetId: Long): Beatmapset {
        canExecute()
        return client.get("$endpoint/beatmapsets/$beatmapsetId").decodeFromJson(kosu)
    }

    @JvmName("search")
    @RequiresOauth
    operator fun get(search: BeatmapsetSearch): BeatmapsetSearchResult {
        canExecute()
        return client.get("$endpoint/beatmapsets/search", emptyMap(), search.getAsQueryParameters())
            .decodeFromJson(kosu)
    }
}