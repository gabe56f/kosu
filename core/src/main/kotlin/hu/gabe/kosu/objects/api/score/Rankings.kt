package hu.gabe.kosu.objects.api.score

import hu.gabe.kosu.objects.api.beatmapset.Beatmapset
import hu.gabe.kosu.objects.api.users.UserStatistics
import kotlinx.serialization.Serializable

@Serializable
data class Rankings(
//    val cursor: Cursor,
    val ranking: List<UserStatistics>,
    val total: Int,
    val beatmapsets: List<Beatmapset>? = emptyList(),
    val spotlights: Spotlight? = null
)

enum class RankingType(internal val stringLiteral: String) {
    SPOTLIGHT("charts"),
    COUNTRY("country"),
    PERFORMANCE("performance"),
    SCORE("score");
}

enum class RankingFilter(internal val stringLiteral: String) {
    ALL("all"),
    FRIENDS("friends");
}