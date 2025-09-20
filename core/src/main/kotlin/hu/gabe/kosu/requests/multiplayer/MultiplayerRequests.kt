package hu.gabe.kosu.requests.multiplayer

import hu.gabe.kosu.Kosu
import hu.gabe.kosu.annotations.RequiresOauth
import hu.gabe.kosu.extensions.decodeFromJson
import hu.gabe.kosu.extensions.get
import hu.gabe.kosu.objects.api.match.MultiplayerScores
import hu.gabe.kosu.objects.api.match.MultiplayerScoresSort
import hu.gabe.kosu.requests.BaseRequestV2

class MultiplayerRequests(kosu: Kosu) : BaseRequestV2(kosu) {
    @RequiresOauth
    fun getScores(
        roomId: Long,
        playlistId: Long,
        limit: Int? = null,
        sort: MultiplayerScoresSort? = null,
        cursorString: String? = null,
    ): MultiplayerScores = client.get(
        "$endpoint/rooms/$roomId/playlist/$playlistId/scores", emptyMap(), listOf(
            "sort" to sort?.stringLiteral,
            "limit" to limit?.toString(),
            "cursor_string" to cursorString
        )
    ).decodeFromJson(kosu)
}