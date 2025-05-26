package hu.gabe.kosu.requests.multiplayer

import hu.gabe.kosu.Kosu
import hu.gabe.kosu.annotations.RequiresOauth
import hu.gabe.kosu.extensions.decodeFromJson
import hu.gabe.kosu.extensions.get
import hu.gabe.kosu.objects.api.beatmapset.SortingOrder
import hu.gabe.kosu.objects.api.match.Match
import hu.gabe.kosu.objects.api.match.MatchEvent
import hu.gabe.kosu.objects.api.users.User
import hu.gabe.kosu.requests.BaseRequestV2
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class MatchRequests(kosu: Kosu) : BaseRequestV2(kosu) {
    @RequiresOauth
    operator fun get(id: Long): MatchResponse = get(id, null, null, null)

    @RequiresOauth
    fun get(
        matchId: Long,
        eventsBeforeId: Long? = null,
        eventsAfterId: Long? = null,
        eventLimit: Int? = null
    ): MatchResponse {
        canExecute()
        if ((eventLimit
                ?: 100) !in 1..101
        ) throw IllegalArgumentException("eventLimit must be between 1 and 101 (inclusive)")
        return client.get(
            "$endpoint/matches/$matchId", emptyMap(), listOf(
                "before" to eventsBeforeId?.toString(),
                "after" to eventsAfterId?.toString(),
                "limit" to eventLimit?.toString()
            )
        ).decodeFromJson(kosu)
    }

    @RequiresOauth
    fun all(limit: Int? = null, sort: SortingOrder? = null, cursorString: String? = null): MatchesResponse {
        canExecute()
        if ((limit ?: 50) !in 1..50) throw IllegalArgumentException("limit must be between 1 and 50 (inclusive)")
        return client.get(
            "$endpoint/matches", emptyMap(), listOf(
                "sort" to sort?.stringLiteral,
                "limit" to limit?.toString(),
                "cursor_string" to cursorString
            )
        ).decodeFromJson(kosu)
    }

    @Serializable
    data class MatchesResponse(
        @SerialName("cursor_string") val cursorString: String,
        val matches: List<Match>
    )

    @Serializable
    data class MatchResponse(
        val match: Match,
        val events: List<MatchEvent>,
        val users: List<User>,
        @SerialName("first_event_id") val firstEventId: Long,
        @SerialName("latest_event_id") val latestEventId: Long
    )
}