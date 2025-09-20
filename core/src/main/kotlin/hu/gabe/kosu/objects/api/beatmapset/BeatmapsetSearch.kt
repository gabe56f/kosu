package hu.gabe.kosu.objects.api.beatmapset

import hu.gabe.kosu.objects.api.beatmap.BeatmapGenre
import hu.gabe.kosu.objects.api.beatmap.BeatmapLanguage
import hu.gabe.kosu.objects.api.beatmap.Ruleset
import hu.gabe.kosu.objects.api.score.Rank
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class BeatmapsetSearch(
    val query: String? = null,
    val sort: BeatmapsetSearchSort? = null,
    val nsfw: Boolean? = null,
    val played: BeatmapsetSearchPlayed? = null,
    val rankAchieved: Rank? = null,
    val mode: Ruleset? = null,
    val language: BeatmapLanguage? = null,
    val genre: BeatmapGenre? = null,
    val extra: List<BeatmapsetSearchExtra>? = emptyList(),
    val generals: List<BeatmapsetSearchGeneral>? = emptyList(),
    val status: BeatmapsetSearchStatus? = null,
    val cursorString: String? = null
) {
    internal fun getAsQueryParameters(): List<Pair<String, String>> {
        val output: MutableList<Pair<String, String>> = ArrayList(4)
        query?.let { output.add("q" to it) }
        sort?.let { output.add("s" to it.stringLiteral) }
        nsfw?.let { output.add("nsfw" to it.toString()) }
        played?.let { output.add("played" to it.stringLiteral) }
        rankAchieved?.let { output.add("r" to it.stringLiteral) }
        mode?.let { output.add("m" to it.id.toString()) }
        language?.let { output.add("l" to it.value.toString()) }
        genre?.let { output.add("g" to it.value.toString()) }
        extra?.let { if (extra.isNotEmpty()) output.add("e" to extra.joinToString(".") { it.stringLiteral }) }
        generals?.let { if (generals.isNotEmpty()) output.add("c" to generals.joinToString(".") { it.stringLiteral }) }
        status?.let { output.add("s" to it.stringLiteral) }
        cursorString?.let { output.add("cursor_string" to it) }

        return output
    }

    class Builder(builderLambda: Builder.() -> Unit) {
        private val extra: MutableList<BeatmapsetSearchExtra> = ArrayList(4)
        private val generals: MutableList<BeatmapsetSearchGeneral> = ArrayList(2)
        var query: String? = null
            private set
        var sort: BeatmapsetSearchSort? = null
            private set
        var nsfw: Boolean? = null
            private set
        var played: BeatmapsetSearchPlayed? = null
            private set
        var rankAchieved: Rank? = null
            private set
        var mode: Ruleset? = null
            private set
        var language: BeatmapLanguage? = null
            private set
        var genre: BeatmapGenre? = null
            private set
        var status: BeatmapsetSearchStatus? = null
            private set
        var cursorString: String? = null
            private set

        fun query(query: String) = apply { this.query = query }
        fun sort(sort: BeatmapsetSearchSort) = apply { this.sort = sort }
        fun nsfw(nsfw: Boolean) = apply { this.nsfw = nsfw }
        fun played(played: BeatmapsetSearchPlayed) = apply { this.played = played }
        fun rankAchieved(rankAchieved: Rank) = apply { this.rankAchieved = rankAchieved }
        fun mode(mode: Ruleset) = apply { this.mode = mode }
        fun language(language: BeatmapLanguage) = apply { this.language = language }
        fun genre(genre: BeatmapGenre) = apply { this.genre = genre }
        fun status(status: BeatmapsetSearchStatus) = apply { this.status = status }
        fun cursorString(cursorString: String) = apply { this.cursorString = cursorString }
        fun extra(extra: BeatmapsetSearchExtra) = apply { this.extra.add(extra) }
        fun general(general: BeatmapsetSearchGeneral) = apply { this.generals.add(general) }

        init {
            builderLambda(this)
        }

        fun build(): BeatmapsetSearch = BeatmapsetSearch(
            query, sort, nsfw, played, rankAchieved, mode, language, genre, extra, generals, status, cursorString
        )

        constructor() : this({})
    }
}

@Serializable
data class SearchInfo(
    val sort: String,
    val limit: Int? = null,
    val start: Int? = null,
    val end: Int? = null,
)

@Serializable
data class BeatmapsetSearchResult(
    val beatmapsets: List<Beatmapset>,
    val search: SearchInfo,
    val total: Int,
    @SerialName("cursor_string") val cursorString: String? = null,
    @SerialName("recommended_difficulty") val recommendedDifficulty: Float? = null,
)

enum class BeatmapsetSearchSort(val stringLiteral: String) {
    ARTIST("artist"),
    CREATOR("creator"),
    DIFFICULTY("difficulty"),
    FAVOURITES("favourites"),
    NOMINATIONS("nominations"),
    PLAYS("plays"),
    RANKED("ranked"),
    RATING("rating"),
    RELEVANCE("relevance"),
    TITLE("title"),
    UPDATED("updated");
}

enum class BeatmapsetSearchPlayed(val stringLiteral: String) {
    ANY("any"),
    NOT_PLAYED("unplayed"),
    PLAYED("played");
}

enum class BeatmapsetSearchExtra(val stringLiteral: String) {
    VIDEO("video"),
    STORYBOARD("storyboard");
}

enum class BeatmapsetSearchGeneral(val stringLiteral: String) {
    RECOMMENDED("recommended"),
    CONVERTS("converts"),
    FOLLOWS("follows"),
    SPOTLIGHTS("spotlights"),
    FEATURED_ARTISTS("featured_artists");
}

enum class BeatmapsetSearchStatus(val stringLiteral: String) {
    ANY("any"),
    HAS_LEADERBOARD("leaderboard"),
    LOVED("loved"),
    QUALIFIED("qualified"),
    PENDING("pending"),
    RANKED("ranked"),
    WIP("wip"),
    GRAVEYARD("graveyard"),
    MY_MAPS("mine"),
    FAVOURITES("favourites");
}