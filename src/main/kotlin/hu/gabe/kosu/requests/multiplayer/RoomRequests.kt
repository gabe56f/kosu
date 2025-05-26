package hu.gabe.kosu.requests.multiplayer

import hu.gabe.kosu.Kosu
import hu.gabe.kosu.annotations.RequiresOauth
import hu.gabe.kosu.extensions.decodeFromJson
import hu.gabe.kosu.extensions.get
import hu.gabe.kosu.objects.api.beatmap.Ruleset
import hu.gabe.kosu.objects.api.match.MultiplayerScores
import hu.gabe.kosu.objects.api.match.MultiplayerScoresSort
import hu.gabe.kosu.objects.api.match.Room
import hu.gabe.kosu.objects.api.match.RoomCategory
import hu.gabe.kosu.objects.api.match.RoomFilterMode
import hu.gabe.kosu.objects.api.match.RoomLeaderboard
import hu.gabe.kosu.objects.api.match.RoomSort
import hu.gabe.kosu.objects.api.match.RoomType
import hu.gabe.kosu.requests.BaseRequestV2

class RoomRequests(kosu: Kosu) : BaseRequestV2(kosu) {
    @RequiresOauth
    operator fun get(roomId: Long): Room {
        canExecute()
        return client.get("$endpoint/rooms/$roomId").decodeFromJson(kosu)
    }

    @RequiresOauth
    fun getLeaderboard(roomId: Long): RoomLeaderboard {
        canExecute()
        return client.get("$endpoint/rooms/$roomId/leaderboard").decodeFromJson(kosu)
    }

    @RequiresOauth
    fun getScores(
        roomId: Long,
        playlistId: Long,
        limit: Int? = null,
        sort: MultiplayerScoresSort? = null,
        cursorString: String? = null,
    ): MultiplayerScores {
        canExecute()
        return client.get(
            "$endpoint/rooms/$roomId/playlist/$playlistId/scores", emptyMap(), listOf(
                "limit" to limit?.toString(),
                "sort" to sort?.stringLiteral,
                "cursor_string" to cursorString,
            )
        ).decodeFromJson(kosu)
    }

    @RequiresOauth
    fun all(
        mode: Ruleset? = null,
        sort: RoomSort? = null,
        limit: Int? = null,
        type: RoomType? = null,
        category: RoomCategory? = null,
        filterMode: RoomFilterMode? = null
    ): List<Room> {
        canExecute()
        return client.get(
            "$endpoint/rooms", emptyMap(), listOf(
                "mode" to mode?.stringLiteral,
                "sort" to sort?.stringLiteral,
                "limit" to limit?.toString(),
                "room_type" to type?.stringLiteral,
                "category" to category?.stringLiteral,
                "filter_mode" to filterMode?.stringLiteral
            )
        ).decodeFromJson(kosu)
    }
}